package cn.aofeng.pssh4j.config;

import java.io.Serializable;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 解析dssh4j.xml后存储在内存中的逻辑结构。
 * 
 * @author <a href="mailto:nieyong@ucweb.com">聂勇</a>
 */
public class Config implements Serializable, Cloneable {

    private static final long serialVersionUID = -1921401434697039148L;
    
    private Map<String, Group> _groups = new ConcurrentHashMap<String, Group>();
    private Map<String, Host> _hosts = new ConcurrentHashMap<String, Host>();
    
    /**
     * 添加主机。
     * 
     * @param host 主机信息。
     * @return 添加成功返回true；否则，返回false。
     */
    public synchronized boolean addHost(Host host) {
        if (! _hosts.containsKey(host.getName())) {
            _hosts.put(host.getName(), host);
        }
        
        return true;
    }
    
    /**
     * 向指定的组添加主机。
     * 
     * @param groupName 组名称。
     * @param host 主机信息。
     * @return 添加成功返回true；否则，返回false。
     */
    public synchronized boolean addHost(String groupName, Host host) {
        addHost(host);
        
        Group group = _groups.get(groupName);
        if (null == group) {
            group = new Group(groupName);
            group.addHost(host.getName());
            _groups.put(groupName, group);
        }
        
        if (! group.constains(host.getName())) {
            group.addHost(host.getName());
        }
        
        return true;
    }
    
    /**
     * 获取指定名称的主机。
     * 
     * @param hostName 主机名称
     * @return 主机信息（{@link Host}）。如果指定名称的主机不存在，返回null。
     */
    public Host getHost(String hostName) {
        return _hosts.get(hostName);
    }
    
    /**
     * 获取所有主机的数量。
     * 
     * @return 所有主机的数量。
     */
    public int getHostCount() {
        return _hosts.size();
    }
    
    /**
     * 获取主机信息集合的迭代器。
     * 
     * @return 主机信息集合的迭代器。
     */
    public synchronized Iterator<Host> hostIterator() {
        return _hosts.values().iterator();
    }
    
    /**
     * 获取指定名称的组。
     * 
     * @param groupName 组名称
     * @return 返回{@link Group}实例。如果不存在，返回null。
     */
    public Group getGroup(String groupName) {
        return _groups.get(groupName);
    }
    
    /**
     * 获取组的数量。
     * 
     * @return 组的数量。
     */
    public int getGroupCount() {
        return _groups.size();
    }
    
    /**
     * 获取组信息集合的迭代器。
     * 
     * @return 组信息集合的迭代器。
     */
    public synchronized Iterator<Group> groupIterator() {
        return _groups.values().iterator();
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((_groups == null) ? 0 : _groups.hashCode());
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
        if (!(obj instanceof Config)) {
            return false;
        }
        Config other = (Config) obj;
        if (_groups == null) {
            if (other._groups != null) {
                return false;
            }
        } else if (!_groups.equals(other._groups)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Config [groups=" + _groups + "]";
    }

}
