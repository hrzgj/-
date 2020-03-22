import java.io.*;
import java.util.Scanner;

/**
 * @author: chenyu
 * @date: 2020/3/13 10:19
 */
public class wc {

        public static void main(String[] args) throws Exception {

            String string="";


            while (true) {
                Scanner scanner = new Scanner(System.in);
                //得到参数
                string = scanner.nextLine();
                //分割前面参数和文件地址，前一个为参数，后一个为文件路径
                args = string.split("\\s+");

                //判断参数是否正确
                if (args.length < 2) {
                    System.out.println("请重新输入正确的参数...");
                    continue;
                }
                //参数类型
                String func = args[0];
                //文件路径
                String filePath = args[1];
                File file = new File(filePath);
                //判断文件是否存在
                if (!file.exists()) {
                    System.out.println("你指定的文件不存在....，请重新指定");
                    continue;
                }

                switch (func) {
                    //统计单词数
                    case "-w":
                        countWord(null, filePath);
                        break;
                    //统计字符数
                    case "-c":
                        countChar(null, filePath);
                        break;
                    //统计文件的 行数
                    case "-l":
                        countLine(null, filePath);
                        break;
                     //递归处理目录的子文件
                    case "-s":
                        countDir(filePath);
                        break;
                    //统计空行代码行注释行
                    case "-a":
                        countFileSpecialLine(filePath);
                        break;
                    default:
                        System.out.println("您输入的参数有误");
                }
            }

        }

        //统计文件行数
        public static void  countLine (String fileName,String absolutePath) throws Exception {
            BufferedReader br=new BufferedReader(new FileReader(new File(absolutePath)));
            String line =null;
            int sum = 0;
            while((line=br.readLine())!=null) {
                sum++;
            }
            System.out.println((fileName==null?"":fileName)+"文件行数为："+sum);
        }

        //统计字符数
        public static void  countChar(String fileName,String path) throws Exception {

            BufferedReader br=new BufferedReader(new FileReader(new File(path)));
            int sum = 0 ;
            String line;
            while ((line = br.readLine()) != null){
                sum+=line.length();
            }
            System.out.println((fileName==null?"":fileName)+"文件字符数为："+sum);

        }

        //统计单词数
        public static void countWord(String fileName,String path) throws Exception {

            BufferedReader br=new BufferedReader(new FileReader(new File(path)));
            //使用正则进行分割：空格 Tab { } ；: ~ ！ ？ ^ % + - * / | & >> >>> << <<< [ ] ( ) \\
            int countWord = 0;
            String str = "";
            while((str = br.readLine()) != null){
                //这里只使用部分符号，还有更多符号没有进行添加
                countWord += str.split("\\s+|\\(|\\)|,|\\.|\\:|\\{|\\}|\\-|\\*|\\+|;|\\?|\\/|\\\\|/").length;
            }


            System.out.println((fileName==null?"":fileName)+"文件单词数为："+countWord);
        }


        //递归处理目录的子文件
        public static void countDir(String filePath) throws Exception {
            File dir = new File(filePath);
            if (!dir.isDirectory() || !dir.exists()){
                System.out.println("你指定的不是目录");
                return;
            }
            File[] files = dir.listFiles();
            assert files != null;
            for (File file : files) {
                if (file.isDirectory()){
                    countDir(file.getAbsolutePath());
                }else {
                    String name = file.getName();
                    String absolutePath = file.getAbsolutePath();
                    //调用其他三个方法
                    countLine(name,absolutePath);
                    countChar(name,absolutePath);
                    countWord(name,absolutePath);
                }
            }

        }

        /**
         * 统计空行，代码行，注释行
         */
        public static void countFileSpecialLine(String filePath) throws IOException {
            //空行
            int spaceNum = 0;
            //注释行
            int noteNum = 0;
            //代码行
            int codeNum = 0;
            BufferedReader br=new BufferedReader(new FileReader(new File(filePath)));
            String line;
            while ( (line = br.readLine()) != null){
                String s = line.replaceAll("[\\s;]", "");
                //空行
                if (s.length() == 0){
                    spaceNum++;
                }else if (s.length() == 1 && ("{".equals(s) || "}".equals(s)) ){
                    spaceNum++;
                }
                //注释行
                else if ( (s.startsWith("{") || s.startsWith("}") ) && s.contains("//")){
                    noteNum++;
                }else if (s.startsWith("//")){
                    noteNum++;
                }else if (s.startsWith("/*") && s.length()>=4 && s.endsWith("*/")){
                    noteNum++;
                }else if (s.startsWith("/*")){
                    noteNum++;
                    while (true){
                        if ((line = br.readLine()) != null){
                            noteNum++;
                            if (line.endsWith("*/")) {
                                break;
                            }
                        }else {
                            break;
                        }
                    }
                }else
                    //代码行
                    {
                    codeNum++;
                }
            }
            System.out.println("文件空行数为："+spaceNum);
            System.out.println("文件注释行数为："+noteNum);
            System.out.println("文件代码行数为："+codeNum);
        }
}
