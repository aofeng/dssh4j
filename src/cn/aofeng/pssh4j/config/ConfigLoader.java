package cn.aofeng.pssh4j.config;

import java.util.Iterator;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import cn.aofeng.common4j.ILifeCycle;
import cn.aofeng.common4j.xml.DomUtil;
import cn.aofeng.common4j.xml.NodeParser;

/**
 * 配置加载器。
 * 
 * @author <a href="mailto:aofengblog@163.com">聂勇</a>
 */
public class ConfigLoader implements ILifeCycle {

    private static Logger _logger = Logger.getLogger(ConfigLoader.class);
    
    private String _configFile = "/dssh4j.xml";
    protected AtomicBoolean _isStarted = new AtomicBoolean(false);
    
    protected Config _config = new Config();
    
    private static ConfigLoader _instance = new ConfigLoader();
    
    private ConfigLoader() {
        // nothing
    }
    
    public static ConfigLoader getInstance() {
        return _instance;
    }
    
    @Override
    public void init() {
        if (_isStarted.get()) {
            return;
        }
        
        Document document = DomUtil.createDocument(_configFile);
        Element rootNode = document.getDocumentElement();
        NodeParser rootParser = new NodeParser(rootNode);
        List<Node> elems = rootParser.getChildNodes();
        
        /* 
         * 循环两遍：第一遍获取所有的host元素，第二遍获取所有的group元素。
         *  避免group元素在host前导致发生初始化结果错误的情况。
         */
        for (Node node : elems) {
            NodeParser nodeParser = new NodeParser(node);
            if ( "host".equals(nodeParser.getName()) ) {
                addHost(nodeParser);
            }  
        }
        for (Node node : elems) {
            NodeParser nodeParser = new NodeParser(node);
            if ( !"group".equals(nodeParser.getName()) ) {
                continue;
            }
            addGroup(nodeParser);
        }
        
        _isStarted.set(true);
        
        Iterator<Host> hostIterator = _config.hostIterator();
        while (hostIterator.hasNext()) {
            _logger.info(hostIterator.next());
        }
        Iterator<Group> groupIterator =  _config.groupIterator();
        while (groupIterator.hasNext()) {
            _logger.info(groupIterator.next());
        }
    }
    
    private void addHost(NodeParser nodeParser) {
        Host host = new Host(nodeParser.getAttributeValue("name"));
        host.setAddress(nodeParser.getChildNodeValue("address"));
        host.setPort( Integer.parseInt(nodeParser.getChildNodeValue("port")) );
        host.setUser(nodeParser.getChildNodeValue("user"));
        host.setPassword(nodeParser.getChildNodeValue("password"));
        
        _config.addHost(host);
    }
    
    private void addGroup(NodeParser groupNodeParser) {
        Iterator<Node> iterator = groupNodeParser.getChildNodes().iterator();
        while (iterator.hasNext()) {
            Node childHostNode = iterator.next();
            NodeParser childHostNodeParser = new NodeParser(childHostNode);
            Host host = _config.getHost(childHostNodeParser.getValue());
            if (null != host) {
                _config.addHost(groupNodeParser.getAttributeValue("name"), host);
            } else{
                _logger.error( String.format("host [%s] not exists, please check the config-file /dssh4j.xml", childHostNodeParser.getValue()) );
            }
        }
    }
    
    @Override
    public void destroy() {
        // nothing
    }

}
