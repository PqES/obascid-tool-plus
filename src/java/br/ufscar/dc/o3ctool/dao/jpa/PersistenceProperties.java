package br.ufscar.dc.o3ctool.dao.jpa;

import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;

public class PersistenceProperties {

    private static final String OPENSHIFT_MYSQL_DB_URL = "OPENSHIFT_MYSQL_DB_URL";

    private static final String OPENSHIFT_MYSQL_DB_USERNAME = "OPENSHIFT_MYSQL_DB_USERNAME";

    private static final String OPENSHIFT_MYSQL_DB_PASSWORD = "OPENSHIFT_MYSQL_DB_PASSWORD";

    /**
     * Procura por propriedades que irão sobrescrever o persistence.xml. A
     * procura é feita na pasta home do usuário, em variáveis de ambiente e em
     * propriedades passadas na inicialização do processo Java.
     *
     * @return Properties propriedades que irão sobrescrever o persistence.xml
     * @throws Exception
     */
    public Properties get() {
        try {
            Properties props = new Properties();

            props.putAll(userHomeJdbcFile());// Digital Ocean

            // Se esse método (systemEnv) retornar alguma propriedade, igual a que o método anterior
            // tenha configurado, ela irá prevalecer.
            props.putAll(systemEnv()); // Heroku

            // Se esse método (javaPropertyJdbcFile()) retornar alguma propriedade, igual a que o método anterior
            // tenha configurado, ela irá prevalecer.
            props.putAll(javaPropertyJdbcFile());

            // Se esse método (javaProperties()) retornar alguma propriedade, igual a que o método anterior
            // tenha configurado, ela irá prevalecer.
            props.putAll(javaProperties());

            return props;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Verifica a existência de um arquivo jdbc.properties na pasta home do
     * usuário. Caso exista, ele irá ler as propriedades que estão dentro dele.
     *
     * A chave das propriedades desse arquivo devem ser iguais ao nome da
     * propriedade do persistence.xml que se deseja sobreescrever.
     *
     * @return Properties propriedades que irão sobrescrever o persistence.xml
     * @throws Exception
     */
    private Properties userHomeJdbcFile() throws Exception {
        Properties props = new Properties();

        File fileProperties = new File(System.getProperty("user.home"), "jdbc.properties");

        if (fileProperties.exists()) {
            props.load(new FileInputStream(fileProperties));
        }

        return props;
    }

    /**
     * Verifica a existência de variáveis de ambiente que serão usadas para
     * sobrescrever as propriedades do persistence.xml.
     *
     * A variável de ambiente para url, usuário e senha devem ter o mesmo nome
     * dos valores das propriedades estáticas
     * {@link #JDBC_URL}, {@link #JDBC_USER} e {@link #JDBC_PASSWORD}
     * respectivamente.
     *
     * @return Properties propriedades que irão sobrescrever o persistence.xml
     * @throws Exception
     */
    private Properties systemEnv() {
        Properties props = new Properties();

        if (System.getenv().containsKey(OPENSHIFT_MYSQL_DB_URL)) {
            props.put("javax.persistence.jdbc.url", System.getenv(OPENSHIFT_MYSQL_DB_URL));
        }

        if (System.getenv().containsKey(OPENSHIFT_MYSQL_DB_USERNAME)) {
            props.put("javax.persistence.jdbc.user", System.getenv(OPENSHIFT_MYSQL_DB_USERNAME));
        }

        if (System.getenv().containsKey(OPENSHIFT_MYSQL_DB_PASSWORD)) {
            props.put("javax.persistence.jdbc.password", System.getenv(OPENSHIFT_MYSQL_DB_PASSWORD));
        }

        return props;
    }

    /**
     * Verifica a existência de um parâmetro Java passado na inicialização da
     * aplicação. Esse parâmetro deve se chamar "jdbc-file", portanto será
     * passado dessa forma "-Djdbc-file=/caminho/jdbc.properites".
     *
     * A chave das propriedades desse arquivo devem ser iguais ao nome da
     * propriedade do persistence.xml que se deseja sobreescrever.
     *
     * @return Properties propriedades que irão sobrescrever o persistence.xml
     * @throws Exception
     */
    private Properties javaPropertyJdbcFile() throws Exception {
        Properties props = new Properties();

        if (!System.getProperties().containsKey("jdbc-file")) {
            return props;
        }

        File fileProperties = new File(System.getProperty("jdbc-file"));

        if (fileProperties.exists()) {
            props.load(new FileInputStream(fileProperties));
        }

        return props;
    }

    /**
     * Verifica a existência de propriedades Java que serão usadas para
     * sobrescrever as propriedades do persistence.xml.
     *
     * A propriedade Java para url, usuário e senha devem ter o mesmo nome dos
     * valores das propriedades estáticas {@link #JDBC_URL}, {@link #JDBC_USER}
     * e {@link #JDBC_PASSWORD} respectivamente.
     *
     * Caso queira sobrescrever a url, por exemplo, a propriedade a ser passada
     * será "-DJDBC_URL=jdbc:mysql://url:porta/banco"
     *
     * @return Properties propriedades que irão sobrescrever o persistence.xml
     * @throws Exception
     */
    private Properties javaProperties() {
        Properties props = new Properties();

        if (System.getProperties().containsKey(OPENSHIFT_MYSQL_DB_URL)) {
            props.put("javax.persistence.jdbc.url", System.getProperty(OPENSHIFT_MYSQL_DB_URL));
        }

        if (System.getProperties().containsKey(OPENSHIFT_MYSQL_DB_USERNAME)) {
            props.put("javax.persistence.jdbc.user", System.getProperty(OPENSHIFT_MYSQL_DB_USERNAME));
        }

        if (System.getProperties().containsKey(OPENSHIFT_MYSQL_DB_PASSWORD)) {
            props.put("javax.persistence.jdbc.password", System.getProperty(OPENSHIFT_MYSQL_DB_PASSWORD));
        }

        return props;
    }
}
