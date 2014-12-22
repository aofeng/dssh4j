package cn.aofeng.pssh4j.progress;

import org.apache.log4j.Logger;

import com.jcraft.jsch.SftpProgressMonitor;

/**
 * SFTP上传/下载进度监控。
 * 
 * @author <a href="mailto:aofengblog@163.com">聂勇</a>
 */
public class SftpProgressMonitorImpl implements SftpProgressMonitor {

    private static Logger _logger = Logger.getLogger(SftpProgressMonitorImpl.class);
    
    private ConsoleProgressBar _progress = null;
    private long _current = 0;
    private String _completeTips = "";
    
    public void setCompleteTips(String completeTips) {
        _completeTips = completeTips;
    }
    
    @Override
    public void init(int op, String src, String dest, long max) {
        _progress = new ConsoleProgressBar(0, max, 50);
    }
    
    @Override
    public boolean count(long count) {
        _current += count;
        _progress.show(_current);

        return true;
    }
    
    @Override
    public void end() {
        if (_logger.isInfoEnabled()) {
            _logger.debug(_completeTips);
        }
    }
}
