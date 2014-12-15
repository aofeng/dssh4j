package cn.aofeng.pssh4j.config;

import java.io.Serializable;

/**
 * 主机信息。
 * 
 * @author <a href="mailto:aofengblog@163.com">聂勇</a>
 */
public class Host implements Serializable, Cloneable {

    private static final long serialVersionUID = 5724074141513284083L;

    /**
     * 地址。
     */
    private String address;
    
    /**
     * 监听端口。
     */
    private int host;
    
    /**
     * 登陆的用户名。
     */
    private String user;
    
    /**
     * 登陆密码。
     */
    private String password;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getHost() {
        return host;
    }

    public void setHost(int host) {
        this.host = host;
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
        result = prime * result + ((address == null) ? 0 : address.hashCode());
        result = prime * result + host;
        result = prime * result
                + ((password == null) ? 0 : password.hashCode());
        result = prime * result + ((user == null) ? 0 : user.hashCode());
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
        if (address == null) {
            if (other.address != null) {
                return false;
            }
        } else if (!address.equals(other.address)) {
            return false;
        }
        if (host != other.host) {
            return false;
        }
        if (password == null) {
            if (other.password != null) {
                return false;
            }
        } else if (!password.equals(other.password)) {
            return false;
        }
        if (user == null) {
            if (other.user != null) {
                return false;
            }
        } else if (!user.equals(other.user)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        StringBuilder buffer = new StringBuilder(128)
            .append("Host [address=").append(address)
            .append(", host=").append(host)
            .append(", user=").append(user)
            .append(", password=").append(password)
            .append("]");
        
        return  buffer.toString();
    }

}
