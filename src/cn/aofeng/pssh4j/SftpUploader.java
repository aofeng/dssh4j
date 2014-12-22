package cn.aofeng.pssh4j;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import org.apache.log4j.Logger;

import cn.aofeng.common4j.io.IOUtil;
import cn.aofeng.pssh4j.config.Host;

import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;
import com.jcraft.jsch.SftpProgressMonitor;

/**
 * SFTP上传文件。
 * 
 * @author <a href="mailto:aofengblog@163.com">聂勇</a>
 */
public class SftpUploader extends AbstractSshConnector {

    private static Logger _logger = Logger.getLogger(SftpUploader.class);
    
    private String _localPath;
    
    private String _remotePath;
    
    public void setLocalPath(String localPath) {
        _localPath = localPath;
    }

    public void setRemotePath(String remotePath) {
        _remotePath = remotePath;
    }
    
    @Override
    protected void run(Session session, Host host) throws Exception {
        ChannelSftp channel = null;
        InputStream ins = null;
        try {
            channel = (ChannelSftp) session.openChannel("sftp");
            channel.connect(5000);
            ins = new FileInputStream(new File(_localPath));
            channel.put(ins, _remotePath, new SftpProgressMonitor() {
                
                ConsoleProgressBar progress = null;
                
                private long current = 0;
                
                @Override
                public void init(int op, String src, String dest, long max) {
                    progress = new ConsoleProgressBar(0, max, 50);
                }
                
                @Override
                public boolean count(long count) {
                    current += count;
                    progress.show(current);

                    return true;
                }
                
                @Override
                public void end() {
                    if (_logger.isInfoEnabled()) {
                        _logger.debug(String.format("download file:%s complete", _remotePath));
                    }
                }
            });
        } catch (JSchException e) {
            _logger.error( String.format("connect machine %s[%s:%d] occurs error", host.getName(), host.getAddress(), host.getPort()), e);
        } catch (SftpException e) {
            _logger.error( String.format("get remote file:%s occurs error", _remotePath), e);
        } catch (FileNotFoundException e) {
            _logger.error( String.format("can not find local file:%s", _localPath), e);
        } finally {
            IOUtil.closeQuietly(ins);
            if (null != channel) {
                channel.disconnect();
            }
        } // end of finally
    }

}
