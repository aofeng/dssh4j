package cn.aofeng.pssh4j;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;

import org.apache.log4j.Logger;

import cn.aofeng.common4j.io.IOUtil;
import cn.aofeng.pssh4j.config.Host;
import cn.aofeng.pssh4j.progress.SftpProgressMonitorImpl;

import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;

/**
 * SFTP下载文件。
 * 
 * @author <a href="mailto:aofengblog@163.com">聂勇</a>
 */
public class SftpDownloader extends AbstractSftpExecutor {

    private final static Logger _logger = Logger.getLogger(SftpDownloader.class);
    
    @Override
    protected void run(Session session, Host host) throws Exception {
        _logger.info( String.format("%s[%s:%d], download file:%s", 
                host.getName(), host.getAddress(), host.getPort(), _remotePath) );
        
        ChannelSftp channel = null;
        OutputStream outs = null;
        try {
            channel = (ChannelSftp) session.openChannel("sftp");
            channel.connect(5000);
            outs = new BufferedOutputStream(new FileOutputStream(
                    new File(_localPath+"."+host.getName())));
            SftpProgressMonitorImpl monitor = new SftpProgressMonitorImpl();
            monitor.setCompleteTips( String.format("download file:%s complete", _remotePath) );
            channel.get(_remotePath, outs, monitor);
        } catch (JSchException e) {
            _logger.error( String.format("connect machine %s[%s:%d] occurs error", host.getName(), host.getAddress(), host.getPort()), e);
        } catch (SftpException e) {
            _logger.error( String.format("download file:%s occurs error", _remotePath), e);
        } catch (FileNotFoundException e) {
            _logger.error( String.format("can not find local file:%s", _localPath), e);
        } finally {
            IOUtil.closeQuietly(outs);
            if (null != channel) {
                channel.disconnect();
            }
        } // end of finally
    }

}
