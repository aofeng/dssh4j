package cn.aofeng.pssh4j;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.log4j.Logger;

import cn.aofeng.common4j.lang.StringUtil;
import cn.aofeng.common4j.lang.SystemUtil;
import cn.aofeng.pssh4j.args.Command;
import cn.aofeng.pssh4j.config.Config;
import cn.aofeng.pssh4j.config.ConfigLoader;
import cn.aofeng.pssh4j.config.Host;

/**
 * Dssh4j的命令解析和执行入口。
 * 
 * @author <a href="mailto:aofengblog@163.com">聂勇</a>
 */
public class Dssh {
    
    private static Logger _logger = Logger.getLogger(Dssh.class);

    public static void main(String[] args) {
        ConfigLoader.getInstance().init();
        
        Options options = createOptions();
        HelpFormatter hf = new HelpFormatter();
        
        CommandLineParser parser = new BasicParser();
        CommandLine cl = null;
        try {
            cl = parser.parse(options, args);
        } catch (ParseException e) {
            _logger.error("parse the arguments occurs error");
            printHelpMsg(options, hf);
        }
        
        if (null == cl) {
            return;
        }
        
        // 没有任何参数，打印帮助信息
        if (cl.getOptions().length <= 0) {
            printHelpMsg(options, hf);
        }
        
        // 打印帮助信息
        if (cl.hasOption(Command.HELP)) {
            printHelpMsg(options, hf);
        }
        
        if (cl.hasOption(Command.OPERATE)) {
            String opVal = cl.getOptionValue(Command.OPERATE);
            if ( Command.OPERATE_CMD.equals(opVal) ) {
                // 执行Shell命令
                String cmd = cl.getOptionValue(Command.OPERATE_CMD);
                CommandExecutor executor = new CommandExecutor();
                executor.setCommands( new String[]{cmd} );
                List<Host> hostList = obtainHostList(cl);
                for (Host host : hostList) {
                    executor.execute(host);
                }
            } else if ( Command.OPERATE_SFTP_UP.equals(opVal) ) {
                // 使用SFTP上传文件
                String localFile = cl.getOptionValue(Command.SFTP_LOCAL_FILE);
                String remoteFile = cl.getOptionValue(Command.SFTP_REMOTE_FILE);
            }  else if ( Command.OPERATE_SFTP_DOWN.equals(opVal) ) {
                // 使用SFTP下载文件
                String localFile = cl.getOptionValue(Command.SFTP_LOCAL_FILE);
                String remoteFile = cl.getOptionValue(Command.SFTP_REMOTE_FILE);
            } else {
                printHelpMsg(options, hf);
            }
        }
        
        ConfigLoader.getInstance().destroy();
    }

    private static List<Host> obtainHostList(CommandLine cl) {
        List<Host> hostList = new LinkedList<Host>();
        
        Config config = ConfigLoader.getInstance().getConfig();
        
        // 所有指定的组的主机
        String groupArgs = cl.getOptionValue(Command.GROUP);
        if (! StringUtil.isBlank(groupArgs)) {
            String[] groupNames = groupArgs.split(",\\|，");
            for (String groupName : groupNames) {
                if (StringUtil.isBlank(groupName)) {
                    continue;
                }
                Iterator<String> hostNames = config.getGroup(groupName).iterator();
                while (hostNames.hasNext()) {
                    String hostName = hostNames.next();
                    hostList.add( config.getHost(hostName) );
                }
            }
        }
        
        // 所有指定的单独的主机
        String hostArgs = cl.getOptionValue(Command.HOST);
        if (! StringUtil.isBlank(hostArgs)) {
            String[] hostNames =  hostArgs.split(",\\|，");
            for (String hostName : hostNames) {
                if (StringUtil.isBlank(hostName)) {
                    continue;
                }
                hostList.add( config.getHost(hostName) );
            }
        }
        
        return hostList;
    }
    
    @SuppressWarnings("static-access")
    private static Options createOptions() {
        Options options = new Options();
        options.addOption(new Option(Command.HELP, false, "print the help message"));
        options.addOption( OptionBuilder.withArgName("group").hasArg().withDescription("one or more group name").create(Command.GROUP) );
        options.addOption( OptionBuilder.withArgName("host").hasArg().withDescription("one or more host name").create(Command.HOST) );
        options.addOption( OptionBuilder.withArgName("cmd | up | down").isRequired().hasArg().withDescription("operate type").create(Command.OPERATE) );
        options.addOption( OptionBuilder.withArgName("command").hasArg().withDescription("execute command on remote machine").create(Command.OPERATE_CMD) );
        options.addOption( OptionBuilder.withArgName("local file").hasArg().withDescription("download file from remote to local via sftp").create(Command.SFTP_LOCAL_FILE) );
        options.addOption( OptionBuilder.withArgName("remote file").hasArg().withDescription("download file from remote to local via sftp").create(Command.SFTP_REMOTE_FILE) );
        
        return options;
    }
    
    private static void printHelpMsg(Options options, HelpFormatter hf) {
        StringBuilder header = new StringBuilder()
            .append(SystemUtil.getEndLine())
            .append("Options:");
        
        StringBuilder footer = new StringBuilder(256)
            .append(SystemUtil.getEndLine())
            .append("Example:")
            .append(SystemUtil.getEndLine())
            .append("\tjava -jar dssh4j.jar -group \"account-sh\"-op cmd -cmd \"ls\"")
            .append(SystemUtil.getEndLine())
            .append("\tjava -jar dssh4j.jar -host \"sh-101\" -op cmd -cmd \"ls\"")
            .append(SystemUtil.getEndLine())
            .append("\tjava -jar dssh4j.jar -group \"account-sh,account-st\" -op up -local /home/nieyong/a.png -remote /home/aofeng/a.png")
            .append(SystemUtil.getEndLine())
            .append("\tjava -jar dssh4j.jar -host \"sh-101,st-101\" -op up -local /home/nieyong/a.png -remote /home/aofeng/a.png")
            .append(SystemUtil.getEndLine())
            .append("\tjava -jar dssh4j.jar -group \"account-sh,account-st\" -op down -local /home/nieyong/a.png -remote /home/aofeng/a.png")
            .append(SystemUtil.getEndLine())
            .append("\tjava -jar dssh4j.jar -host \"sh-101,st-101\" -op download -local /home/nieyong/a.png -remote /home/aofeng/a.png");
        
        hf.printHelp("java -jar dssh4j.jar [Options]", header.toString(), options, footer.toString());
    }

}
