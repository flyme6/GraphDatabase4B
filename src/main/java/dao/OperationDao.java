package dao;

import org.omg.CORBA.Object;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Scanner;

public class OperationDao {
    private String table;
    private SQLDao dao;
    private Vertex vertex;
    private Edge edge;
    public OperationDao(String table, SQLDao dao, Vertex vertex, Edge edge){
        this.table = table;
        this.dao = dao;
        this.vertex = vertex;
        this.edge = edge;
        opration();
    }
//    关系逻辑混乱无检查
//    主键自增导致ID不连续（删除某些ID无法自动填补）


    public  void opration(){
        while (true){
            System.out.println("请输入操作代号：1.增加；2.修改；3.删除；4.查询；5.返回上一界面；");

            try {
//                下面的语句是否可以放到try前
                Scanner scanner = new Scanner(System.in);
                int operationCode = scanner.nextInt();
                if (operationCode == 1 && table.equalsIgnoreCase("vertex2")) {//插入vertex
                    System.out.println("NAME:");
                    vertex.setName(scanner.next());
                    System.out.println("AGE:");
                    int age = scanner.nextInt();
                    if (age <= 0) {
                        System.out.println("无效年龄，请核对后重新输入..."+"\n");
                        continue;
                    }
                    vertex.setAge(age);
                    System.out.println("SEX(female 1;male 0):");
                    int sex = scanner.nextInt();
                    if (sex != 0 && sex != 1) {
                        System.out.println("无效性别，请核对后重新输入..."+"\n");
                        continue;
                    }
                    vertex.setSex(sex);
                    System.out.println("SALARY(yuan):");
                    double salary = scanner.nextDouble();
                    if (salary < 0) {
                        System.out.println("无效薪资，请核对后重新输入..."+"\n");
                        continue;
                    }
                    vertex.setSalary(salary);
//                vertex.setId(dao.maxIdOfVertex()+1);
//                dao.insertVertex(vertex.getId(),vertex.getName(),vertex.getAge(),vertex.getSex(),vertex.getSalary());
                    dao.insertVertex(vertex);
                    System.out.println("插入成功！"+"\n");
                } else if (operationCode == 1 && table.equalsIgnoreCase("edge2")) {//插入edge
                    ArrayList<Integer> idTemp = dao.selectAllIdOf("vertex2");
                    System.out.println("ORIGIN_ID");
                    int oId = scanner.nextInt();
                    if (!idTemp.contains(oId)) {
                        System.out.println("不存在该点，请核对后输入..."+"\n");
                        continue;
                    }
                    edge.setOrigin_Id(oId);
                    System.out.println("TERMINUS_ID");
                    int tId = scanner.nextInt();
                    if (!idTemp.contains(tId)) {
                        System.out.println("不存在该点，请核对后输入..."+"\n");
                        continue;
                    }
                    if (tId == oId) {
                        System.out.println("不能与自身产生关系，请核对后输入..."+"\n");
                        continue;
                    }
                    edge.setTerminus_Id(tId);
                    System.out.println("RELATION_ID:");
                    System.out.println(edge.getRelationList());
                    int rId = scanner.nextInt();
                    if (rId <= 0 || rId > edge.getRelationList().size()) {
                        System.out.println("不存在该关系，请核对后输入..."+"\n");
                        continue;
                    }
                    edge.setRelation_Id(rId);
//                Object o = dao.maxIdOfEdge();
//                if (o instanceof Integer){
//                    edge.setId(((Integer) o).intValue()+1);
//                }else{
//                    edge.setId(-1);
//                }
//
                    dao.insertEdge(edge);
                    System.out.println("插入成功！"+"\n");
                } else if (operationCode == 3) {
                    System.out.println("请输入需要删除的数据ID：");
                    int id = scanner.nextInt();
                    if (table.equals("vertex2")&&!dao.selectAllIdOf("vertex2").contains(id)){
                        System.out.println("该表中无此数据，请核对后输入..."+"\n");
                        continue;
                    }
                    if (table.equals("edge2")&&!dao.selectAllIdOf("edge2").contains(id)){
                        System.out.println("该表中无此数据，请核对后输入..."+"\n");
                        continue;
                    }
                    dao.deleteById(table, scanner.nextInt());
                    System.out.println("删除成功"+"\n");

                } else if (operationCode == 2) {
                    //!!!!有问题
                    System.out.println("请输入需要修改的数据ID：");
                    int id = scanner.nextInt();
                    System.out.println("请输入需要修改的字段：");
                    String fieldName = scanner.next();
//                    if(fieldName=="NAME"){
//                        String name = scanner.next();
//                        dao.updateById(name);

//                    }
//                    if (fieldName=="AGE"){
//                        int age = scanner.nextInt();
//                        dao.updateById(age);
//                    }
                    System.out.println("请输入字段新值：");
//                dao.updateById(table,fieldName,(Object)scanner.next(),id);

                } else if (operationCode == 4) {
                    System.out.println("输入查询代号：1.简单查询；2.联表查询");
                    if (scanner.nextInt() == 1) {
                        System.out.println("输入需要查询的ID(查看所有输入*)：");
                        String temp = scanner.next();
                        if (temp.equals("*") && table.equals("vertex2")) {//查询vertex表中所有
                            LinkedList<Vertex> tempList = dao.selectAllOfVertex(table);
                            if (tempList.size() == 0) {
                                System.out.println("没有记录...");
                            } else {
                                for (Vertex v : tempList)
                                    System.out.println("ID=" + v.getId() + " NAME=" + v.getName()
                                            + " AGE=" + v.getAge() + " SEX=" + v.getSex() + " SALARY=" + v.getSalary());
                            }
                        } else if (temp.equals("*") && table.equals("edge2")) {//查询edge表中所有
                            LinkedList<Edge> tempList = dao.selectAllOfEdge(table);
                            if (tempList.size() == 0) {
                                System.out.println("没有记录..."+"\n");
                            } else {
//                        ！！！！查询结果出错
                                for (Edge e : tempList)
                                    System.out.println("ID=" + e.getId() + " ORIGIN_ID=" + e.getOriginId()
                                            + " TERMINUS_ID=" + e.getTerminusId() + " RELATION=" + e.getRelationList().get(e.getRelationId()));
                            }

                        } else {//查询任意表的某条记录
                            System.out.println(dao.selectById(table, Integer.valueOf(temp).intValue()));
                        }

                    } else {
                        System.out.println("输入代号1.查询职员N阶关系；");
                        if (scanner.nextInt() == 1) {
                            System.out.println("输入起始点：");
                            int idTemp = scanner.nextInt();
                            System.out.println("输入阶数：");
                            int N = scanner.nextInt();
                            System.out.println("输入查询关系：" + edge.getRelationList());
                            int relationTemp = scanner.nextInt();
                            int count = 0;
                            int flag = 0;
                            count=find(N, dao.selectAVertexById("vertex2", idTemp), count,flag);
                            System.out.println("该点共有"+count+"个"+N+"阶关系"+"\n");
                            if (count==0){
                                System.out.println("该点无"+N+"阶此关系！"+"\n");
                            }

                        }
                    }

                } else if (operationCode == 5) {
                    break;
                }
//            System.out.println("请输入操作代号：1.增加；2.修改；3.删除；4.查询；5.返回上一界面；");
//            operationCode = scanner.nextInt();
            }catch (Exception E){
                System.out.println("请按要求输入...");
                continue;
            }
        }

    }

    /***
     * 递归查找某点N阶关系
     * @param N
     * @param v
     * @param count
     * @param flag
     * @return
     */
    public int find(int N, Vertex v, int count,int flag){

        if (N==flag){
            System.out.println("与起始点是"+N+"阶关系的点：  ID="+ v.getId()+
                    "   NAME="+v.getName()+"   AGE="+v.getAge()+"   SEX="+
                    v.getSex()+"   SALARY="+v.getSalary());
            count++;
            return count;
        }
        ArrayList<Edge> edges = dao.selectAllOfEdgeByORINGIN("edge2",v.getId());
        if (edges.size()==0){
            return count;
        }
        for (Edge edge:edges){
            ArrayList<Vertex> vertexs = dao.selectAllOfVertexByTer("vertex2",edge.getTerminusId());
            for (Vertex vertex:vertexs){
                count=find(N,vertex,count,++flag);
            }
            flag = 0;
        }

        return count;
    }

}
