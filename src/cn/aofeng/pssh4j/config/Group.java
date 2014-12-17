package cn.aofeng.pssh4j.config;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * 组。每个组包含一个或多个Host。
 * 
 * @author <a href="mailto:aofengblog@163.com">聂勇</a>
 */
public class Group implements Serializable, Cloneable {

    private static final long serialVersionUID = 8777149011045000148L;
    
    private String _name;
    private Set<String> _hosts = new HashSet<String>();
    
    public Group(String name) {
        _name = name;
    }
    
    /**
     * 获取组名称。
     * 
     * @return 组名称。
     */
    public String getName() {
        return _name;
    }
    
    /**
     * 添加主机。
     * 
     * @param hostName 主机名称
     * @return 添加的结果。成功返回true，否则返回false。
     */
    public synchronized boolean addHost(String hostName) {
        boolean result = _hosts.add(hostName);
        
        return result;
    }
    
    /**
     * 查询是否包含指定的主机。
     * 
     * @param hostName 主机名称
     * @return 如果包含指定的主机，返回true；否则，返回false。
     */
    public boolean constains(String hostName) {
        return _hosts.contains(hostName);
    }
    
    /**
     * 获取当前组所属的主机数量。
     * 
     * @return 当前组所属的主机数量。
     */
    public int getHostCount() {
        return _hosts.size();
    }
    
    /**
     * 获取主机名称列表的迭代器。
     * 
     * @return 主机名称列表的迭代器。
     */
    public synchronized Iterator<String> iterator() {
        return _hosts.iterator();
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result
                + ((_name == null) ? 0 : _name.hashCode());
        
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
        if (!(obj instanceof Group)) {
            return false;
        }
        Group other = (Group) obj;
        if (_name == null) {
            if (other._name != null) {
                return false;
            }
        } else if (!_name.equals(other._name)) {
            return false;
        }
        
        return true;
    }

    @Override
    public String toString() {
        return "Group [name=" + _name + ", hosts=" + _hosts + "]";
    }

    
}
