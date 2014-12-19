package cn.aofeng.pssh4j.args;

/**
 * dssh4j支持的指令。
 * 
 * @author <a href="mailto:nieyong@ucweb.com">聂勇</a>
 */
public class Command {

    /**
     * 打印帮助信息。
     */
    public final static String HELP = "help";
    
    /**
     * 组和主机信息配置文件。
     */
    public final static String CONFILE_FILE = "file";
    
    /**
     * 组。
     */
    public final static String GROUP = "group";
    
    /**
     * 主机。
     */
    public final static String HOST = "host";
    
    /**
     * 操作类型。
     */
    public final static String OPERATE = "op";
    
    /**
     * 远程执行指令。
     */
    public final static String OPERATE_CMD = "cmd";
    
    /**
     * 使用SFTP上传文件。
     */
    public final static String OPERATE_SFTP_UP = "up";
    
    /**
     * 使用SFTP下载文件。
     */
    public final static String OPERATE_SFTP_DOWN = "down";
    
    /**
     * 本地文件绝对地址。
     */
    public final static String SFTP_LOCAL_FILE = "local";
    
    /**
     * 远程文件绝对地址。
     */
    public final static String SFTP_REMOTE_FILE = "remote";

}
