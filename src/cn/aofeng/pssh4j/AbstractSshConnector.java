package cn.aofeng.pssh4j;

import java.util.Properties;

import org.apache.log4j.Logger;

import cn.aofeng.pssh4j.config.Host;

import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

/**
 * 抽象SSH连接器。
 * 
 * @author <a href="mailto:aofengblog@163.com">聂勇</a>
 */
public abstract class AbstractSshConnector {

    private final static Logger _logger = Logger.getLogger(AbstractSshConnector.class);
    
    protected abstract void run(Session session, Host host) throws Exception; 
    
    public void execute(Host host) {
        if (null == host) {
            _logger.error("invalid host information:null");
            return;
        }
        
        JSch jsch = new JSch();
        Session session  = null;
        try {
            session = jsch.getSession(host.getUser(), host.getAddress(), host.getPort());
            session.setPassword(host.getPassword());
            Properties props = new Properties();
            props.put("StrictHostKeyChecking", "no");
            session.setConfig(props);
            session.connect(5000); // 毫秒
            run(session, host);
        } catch (JSchException e) {
            _logger.error( String.format("connect remote machine %s[%s:%d] occurs error", host.getName(), host.getAddress(), host.getPort()), e);
        } catch (Exception e) {
            _logger.error( String.format("connect remote machine %s[%s:%d] occurs error", host.getName(), host.getAddress(), host.getPort()), e);
        } finally {
            if (null != session) {
                session.disconnect();
            }
        }
    }

}
