package cn.aofeng.pssh4j;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.List;

import org.apache.log4j.Logger;

import cn.aofeng.common4j.lang.SystemUtil;
import cn.aofeng.pssh4j.config.Host;

import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.Session;

/**
 * 执行Shell命令。
 * 
 * @author <a href="mailto:aofengblog@163.com">聂勇</a>
 */
public class CommandExecutor extends AbstractSshConnector {

    private static Logger _logger = Logger.getLogger(CommandExecutor.class);
    
    private String _charset = "UTF-8";
    private int _connectTimeout = 5000;   // 单位：毫秒
    
    private String[] _commands;
    
    public void setCommands(String[] commands) {
        _commands = commands;
    }
    
    public void setCommands(List<String> commandList) {
        _commands = new String[commandList.size()];
        commandList.toArray(_commands);
    }
    
    private byte[] convert(String[] commands) throws UnsupportedEncodingException, IOException {
        ByteArrayOutputStream outs = new ByteArrayOutputStream(128);
        for (String command : commands) {
            outs.write(getBytes(command));
            outs.write(getBytes(SystemUtil.getEndLine()));
        }
        
        return outs.toByteArray();
    }
    
    private byte[] getBytes(String src) throws UnsupportedEncodingException {
        return src.getBytes(_charset);
    }
    
    @Override
    protected void run(Session session, Host host) throws Exception {
        _logger.info( String.format("%s[%s:%d], execute command:%s", 
                host.getName(), host.getAddress(), host.getPort(), Arrays.toString(_commands)) );
        
        ChannelExec channel = null;
        try {
            channel = (ChannelExec) session.openChannel("exec");
            channel.setPtyType("xterm");
            channel.setEnv("LANG", "en_US.UTF-8");
            channel.setCommand(convert(_commands));
            channel.setErrStream(System.err);
            channel.connect(_connectTimeout);
            
            InputStream ins = channel.getInputStream();
            ByteArrayOutputStream bytesArray = new ByteArrayOutputStream(1024);
            byte[] temp =new byte[1024];
            while (true) {
                while (ins.available() > 0) {
                    int readNum = ins.read(temp);
                    if (-1 == readNum) {
                        break;
                    }
                    
                    bytesArray.write(temp, 0, readNum);
                }
                if (channel.isClosed() && ins.available() <= 0) {
                    break;
                }
                Thread.sleep(500);
            }
            
            _logger.info( new String(bytesArray.toByteArray(), _charset) );
        } catch (Exception e) {
            _logger.error("execute command occurs error", e);
        } finally {
            if (null != channel) {
                channel.disconnect();
            }
        }
    }

}
