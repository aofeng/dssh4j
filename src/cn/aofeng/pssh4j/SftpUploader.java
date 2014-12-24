package cn.aofeng.pssh4j;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import org.apache.log4j.Logger;

import cn.aofeng.common4j.io.IOUtil;
import cn.aofeng.pssh4j.config.Host;
import cn.aofeng.pssh4j.progress.SftpProgressMonitorImpl;

import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;

/**
 * SFTP上传文件。
 * 
 * @author <a href="mailto:aofengblog@163.com">聂勇</a>
 */
public class SftpUploader extends AbstractSftpExecutor {

    private static Logger _logger = Logger.getLogger(SftpUploader.class);
    
    @Override
    protected void run(Session session, Host host) throws Exception {
        _logger.info( String.format("%s[%s:%d], upload file:%s", 
                host.getName(), host.getAddress(), host.getPort(), _localPath) );
        
        ChannelSftp channel = null;
        InputStream ins = null;
        try {
            channel = (ChannelSftp) session.openChannel("sftp");
            channel.connect(5000);
            ins = new BufferedInputStream(new FileInputStream(
                    new File(_localPath)));
            SftpProgressMonitorImpl monitor = new SftpProgressMonitorImpl();
            monitor.setCompleteTips( String.format("upload file:%s complete", _localPath) );
            channel.put(ins, _remotePath, monitor);
        } catch (JSchException e) {
            _logger.error( String.format("connect machine %s[%s:%d] occurs error", host.getName(), host.getAddress(), host.getPort()), e);
        } catch (SftpException e) {
            _logger.error( String.format("upload file:%s occurs error", _remotePath), e);
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
