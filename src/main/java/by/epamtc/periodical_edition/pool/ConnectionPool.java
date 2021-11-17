package by.epamtc.periodical_edition.pool;

import java.sql.Array;
import java.sql.Blob;
import java.sql.CallableStatement;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.NClob;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLClientInfoException;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.SQLXML;
import java.sql.Savepoint;
import java.sql.Statement;
import java.sql.Struct;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;

public class ConnectionPool {
    private final static int DEFAULT_MAX_POOL_SIZE = 100;
    private final static int DEFAULT_MAX_IDLE = 15;
    private final static int DEFAULT_MAX_WAIT = 2;
    private BlockingQueue<Connection> connectionsQueue;
    private BlockingQueue<Connection> takenAwayConnectionsQueue;
    private String driverClassName;
    private String url;
    private String userName;
    private String password;
    private int maxPoolSize;
    private int maxIdle;
    private int maxWait;
    private boolean isPoolClosing;

    private ConnectionPool() {
        DBResourceManager dbResourceManager = DBResourceManager.getInstance();
        this.driverClassName = dbResourceManager.retrieveValue(DBParameter.DB_DRIVER);
        this.url = dbResourceManager.retrieveValue(DBParameter.DB_URL);
        this.userName = dbResourceManager.retrieveValue(DBParameter.DB_USERNAME);
        this.password = dbResourceManager.retrieveValue(DBParameter.DB_PASSWORD);
        try {
            this.maxPoolSize = Integer.parseInt(dbResourceManager.retrieveValue(DBParameter.DB_MAX_POOL_SIZE));
            this.maxIdle = Integer.parseInt(dbResourceManager.retrieveValue(DBParameter.DB_MAX_IDLE));
            this.maxWait = Integer.parseInt(dbResourceManager.retrieveValue(DBParameter.DB_MAX_WAIT));
        } catch (NumberFormatException e) {
            this.maxPoolSize = DEFAULT_MAX_POOL_SIZE;
            this.maxIdle = DEFAULT_MAX_IDLE;
            this.maxWait = DEFAULT_MAX_WAIT;
        }
    }

    private static class SingletonHelper {
        private final static ConnectionPool INSTANCE = new ConnectionPool();
    }

    public static ConnectionPool retrieveConnectionPool() {
        return SingletonHelper.INSTANCE;
    }

    public void initPool() {
        Locale.setDefault(Locale.ENGLISH);

        try {
            Class.forName(driverClassName);
            this.connectionsQueue = new ArrayBlockingQueue<Connection>(maxPoolSize);
            this.takenAwayConnectionsQueue = new ArrayBlockingQueue<Connection>(maxPoolSize);
            for (int i = 0; i < maxIdle; i++) {
                connectionsQueue.add(openConnection());
            }
        } catch (ClassNotFoundException e) {
            throw new InitConnectionPoolException("Database driver class wasn't found.", e);
        }
        isPoolClosing = false;
    }

    private PooledConnection openConnection() {
        try {
            return new PooledConnection(DriverManager.getConnection(url, userName, password));
        } catch (SQLException e) {
            throw new InitConnectionPoolException(e);
        }
    }

    public Connection retrieveConnection() throws ConnectionPoolException {
        Connection connection = null;
        if (isPoolClosing) {
            return connection;
        }
        try {
            if (connectionsQueue.size() == 0 && takenAwayConnectionsQueue.size() < maxPoolSize) {
                connection = openConnection();
            } else {
                connection = connectionsQueue.poll(maxWait, TimeUnit.SECONDS);
            }
            takenAwayConnectionsQueue.add(connection);
        } catch (InterruptedException e) {
            throw new ConnectionPoolException(e);
        }
        return connection;
    }

    public void clearConnectionPool() throws ConnectionPoolException {
        isPoolClosing = true;
        closeConnectionsQueue(connectionsQueue);
        closeConnectionsQueue(takenAwayConnectionsQueue);
    }

    private void closeConnectionsQueue(BlockingQueue<Connection> connectionsQueue) throws ConnectionPoolException {
        Connection connection;
        try {
            while ((connection = connectionsQueue.poll()) != null && !connection.isClosed()) {
                if (!connection.getAutoCommit()) {
                    connection.commit();
                }
                ((PooledConnection) connection).reallyClose();
                connection = connectionsQueue.poll();
            }
        } catch (SQLException e) {
            throw new ConnectionPoolException(e);
        }
    }

    public void closeConnection(Connection connection) throws ConnectionPoolException {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                throw new ConnectionPoolException(e);
            }
        }
    }

    public void closeConnection(Connection connection, Statement statement) throws ConnectionPoolException {
        if (statement != null) {
            try {
                statement.close();
            } catch (SQLException e) {
                throw new ConnectionPoolException(e);
            }
        }

        closeConnection(connection);
    }

    public void closeConnection(Connection connection, Statement statement, ResultSet resultSet)
            throws ConnectionPoolException {
        if (resultSet != null) {
            try {
                resultSet.close();
            } catch (SQLException e) {
                throw new ConnectionPoolException(e);
            }
        }
        closeConnection(connection, statement);
    }

    private class PooledConnection implements Connection {
        private Connection connection;

        public PooledConnection(Connection connection) throws SQLException {
            this.connection = connection;
            connection.setAutoCommit(true);
        }

        public void reallyClose() throws SQLException {
            connection.close();
        }

        @Override
        public void close() throws SQLException {

            if (connection != null && !connection.isClosed()) {

                if (connection.isReadOnly()) {
                    connection.setReadOnly(false);
                }
            }

            if (!takenAwayConnectionsQueue.remove(this)) {
                throw new SQLException("Queue of taken away connections doesn't contain passed connection.");
            }
            if (!connectionsQueue.offer(this)) {
                throw new SQLException("Pool can't accept passed connection.");
            }

        }

        @Override
        public <T> T unwrap(Class<T> iface) throws SQLException {
            return connection.unwrap(iface);
        }

        @Override
        public boolean isWrapperFor(Class<?> iface) throws SQLException {
            return connection.isWrapperFor(iface);
        }

        @Override
        public Statement createStatement() throws SQLException {
            return connection.createStatement();
        }

        @Override
        public PreparedStatement prepareStatement(String sql) throws SQLException {
            return connection.prepareStatement(sql);
        }

        @Override
        public CallableStatement prepareCall(String sql) throws SQLException {
            return connection.prepareCall(sql);
        }

        @Override
        public String nativeSQL(String sql) throws SQLException {
            return connection.nativeSQL(sql);
        }

        @Override
        public void setAutoCommit(boolean autoCommit) throws SQLException {
            connection.setAutoCommit(autoCommit);

        }

        @Override
        public boolean getAutoCommit() throws SQLException {
            return connection.getAutoCommit();
        }

        @Override
        public void commit() throws SQLException {
            connection.commit();

        }

        @Override
        public void rollback() throws SQLException {
            connection.rollback();

        }

        @Override
        public boolean isClosed() throws SQLException {
            return connection.isClosed();
        }

        @Override
        public DatabaseMetaData getMetaData() throws SQLException {
            return connection.getMetaData();
        }

        @Override
        public void setReadOnly(boolean readOnly) throws SQLException {
            connection.setReadOnly(readOnly);

        }

        @Override
        public boolean isReadOnly() throws SQLException {
            return connection.isReadOnly();
        }

        @Override
        public void setCatalog(String catalog) throws SQLException {
            connection.setCatalog(catalog);

        }

        @Override
        public String getCatalog() throws SQLException {
            return connection.getCatalog();
        }

        @Override
        public void setTransactionIsolation(int level) throws SQLException {
            connection.setTransactionIsolation(level);

        }

        @Override
        public int getTransactionIsolation() throws SQLException {
            return connection.getTransactionIsolation();
        }

        @Override
        public SQLWarning getWarnings() throws SQLException {
            return connection.getWarnings();
        }

        @Override
        public void clearWarnings() throws SQLException {
            connection.clearWarnings();

        }

        @Override
        public Statement createStatement(int resultSetType, int resultSetConcurrency) throws SQLException {
            return connection.createStatement(resultSetType, resultSetConcurrency);
        }

        @Override
        public PreparedStatement prepareStatement(String sql, int resultSetType, int resultSetConcurrency)
                throws SQLException {
            return connection.prepareStatement(sql, resultSetType, resultSetConcurrency);
        }

        @Override
        public CallableStatement prepareCall(String sql, int resultSetType, int resultSetConcurrency)
                throws SQLException {
            return connection.prepareCall(sql, resultSetType, resultSetConcurrency);
        }

        @Override
        public Map<String, Class<?>> getTypeMap() throws SQLException {
            return connection.getTypeMap();
        }

        @Override
        public void setTypeMap(Map<String, Class<?>> map) throws SQLException {
            connection.setTypeMap(map);

        }

        @Override
        public void setHoldability(int holdability) throws SQLException {
            connection.setHoldability(holdability);

        }

        @Override
        public int getHoldability() throws SQLException {
            return connection.getHoldability();
        }

        @Override
        public Savepoint setSavepoint() throws SQLException {
            return connection.setSavepoint();
        }

        @Override
        public Savepoint setSavepoint(String name) throws SQLException {
            return connection.setSavepoint(name);
        }

        @Override
        public void rollback(Savepoint savepoint) throws SQLException {
            connection.rollback(savepoint);

        }

        @Override
        public void releaseSavepoint(Savepoint savepoint) throws SQLException {
            connection.releaseSavepoint(savepoint);

        }

        @Override
        public Statement createStatement(int resultSetType, int resultSetConcurrency, int resultSetHoldability)
                throws SQLException {
            return connection.createStatement(resultSetType, resultSetConcurrency, resultSetHoldability);
        }

        @Override
        public PreparedStatement prepareStatement(String sql, int resultSetType, int resultSetConcurrency,
                                                  int resultSetHoldability) throws SQLException {
            return connection.prepareStatement(sql, resultSetType, resultSetConcurrency, resultSetHoldability);
        }

        @Override
        public CallableStatement prepareCall(String sql, int resultSetType, int resultSetConcurrency,
                                             int resultSetHoldability) throws SQLException {
            return connection.prepareCall(sql, resultSetType, resultSetConcurrency, resultSetHoldability);
        }

        @Override
        public PreparedStatement prepareStatement(String sql, int autoGeneratedKeys) throws SQLException {
            return connection.prepareStatement(sql, autoGeneratedKeys);
        }

        @Override
        public PreparedStatement prepareStatement(String sql, int[] columnIndexes) throws SQLException {
            return connection.prepareStatement(sql, columnIndexes);
        }

        @Override
        public PreparedStatement prepareStatement(String sql, String[] columnNames) throws SQLException {
            return connection.prepareStatement(sql, columnNames);
        }

        @Override
        public Clob createClob() throws SQLException {
            return connection.createClob();
        }

        @Override
        public Blob createBlob() throws SQLException {
            return connection.createBlob();
        }

        @Override
        public NClob createNClob() throws SQLException {
            return connection.createNClob();
        }

        @Override
        public SQLXML createSQLXML() throws SQLException {
            return connection.createSQLXML();
        }

        @Override
        public boolean isValid(int timeout) throws SQLException {
            return connection.isValid(timeout);
        }

        @Override
        public void setClientInfo(String name, String value) throws SQLClientInfoException {
            connection.setClientInfo(name, value);

        }

        @Override
        public void setClientInfo(Properties properties) throws SQLClientInfoException {
            connection.setClientInfo(properties);

        }

        @Override
        public String getClientInfo(String name) throws SQLException {
            return connection.getClientInfo(name);
        }

        @Override
        public Properties getClientInfo() throws SQLException {
            return connection.getClientInfo();
        }

        @Override
        public Array createArrayOf(String typeName, Object[] elements) throws SQLException {
            return connection.createArrayOf(typeName, elements);
        }

        @Override
        public Struct createStruct(String typeName, Object[] attributes) throws SQLException {
            return connection.createStruct(typeName, attributes);
        }

        @Override
        public void setSchema(String schema) throws SQLException {
            connection.setSchema(schema);

        }

        @Override
        public String getSchema() throws SQLException {
            return connection.getSchema();
        }

        @Override
        public void abort(Executor executor) throws SQLException {
            connection.abort(executor);

        }

        @Override
        public void setNetworkTimeout(Executor executor, int milliseconds) throws SQLException {
            connection.setNetworkTimeout(executor, milliseconds);

        }

        @Override
        public int getNetworkTimeout() throws SQLException {
            return connection.getNetworkTimeout();
        }
    }

}