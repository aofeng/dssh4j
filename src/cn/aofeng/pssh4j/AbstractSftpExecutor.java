package cn.aofeng.pssh4j;;

/**
 * SFTP操作抽象类。
 * 
 * @author <a href="mailto:nieyong@ucweb.com">聂勇</a>
 */
public abstract class AbstractSftpExecutor extends AbstractSshConnector {

    protected String _localPath;
    
    protected String _remotePath;
    
    public void setLocalPath(String localPath) {
        _localPath = localPath;
    }

    public void setRemotePath(String remotePath) {
        _remotePath = remotePath;
    }

}
