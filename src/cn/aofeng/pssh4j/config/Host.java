package cn.aofeng.pssh4j.config;

import java.io.Serializable;

/**
 * 主机信息。
 * 
 * @author <a href="mailto:aofengblog@163.com">聂勇</a>
 */
public class Host implements Serializable, Cloneable {

    private static final long serialVersionUID = 1014861600634195011L;

    /**
     * 主机名称。
     */
    private String name;
    
    /**
     * 地址。
     */
    private String address;
    
    /**
     * 监听端口。
     */
    private int port;
    
    /**
     * 登陆的用户名。
     */
    private String user;
    
    /**
     * 登陆密码。
     */
    private String password;

    public Host(String name) {
        this.name = name;
    }
    
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        result = prime * result + ((address == null) ? 0 : address.hashCode());
        result = prime * result + port;
        
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof Host)) {
            return false;
        }
        
        Host other = (Host) obj;
        if (name == null) {
            if (other.name != null) {
                return false;
            }
        } else if (!name.equals(other.name)) {
            return false;
        }
        if (address == null) {
            if (other.address != null) {
                return false;
            }
        } else if (!address.equals(other.address)) {
            return false;
        }
        if (port != other.port) {
            return false;
        }
        
        return true;
    }

    @Override
    public String toString() {
        StringBuilder buffer = new StringBuilder(128)
            .append("Host [name=").append(name)
            .append(", address=").append(address)
            .append(", port=").append(port)
            .append("]");
        
        return  buffer.toString();
    }

}
