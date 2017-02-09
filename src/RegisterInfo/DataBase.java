package RegisterInfo;

import java.util.ArrayList;

public class DataBase {

   private Node headNode;
   private ArrayList<Node> subject = new ArrayList<Node>();

   public void setHeadNode(String depth, String classification, String name, String grade, String time, String previous, String term) {
      headNode = new Node(depth, classification, name, grade, time, previous, term);
   }

   public void addNode(String depth, String classification, String name, String grade, String time, String previous, String term) {
      subject.add(new Node(depth, classification, name, grade, time, previous, term));
   }

   public int NumberOfNode() {
      return subject.size();
   }

   // getter

   public Node getHeadNode() {
      return headNode;
   }

   // i번째 노드 반환
   public Node getNode(int i) {
      return subject.get(i);
   }

   // i번째 노드의 인자 반환
   public String getName(int i) {
      return subject.get(i).getName();
   }

   public String getDepth(int i) {
      return subject.get(i).getDepth();
   }

   public String getClassification(int i) {
      return subject.get(i).getClassification();
   }

   public String getGrade(int i) {
      return subject.get(i).getGrade();
   }

   public String getTime(int i) {
      return subject.get(i).getTime();
   }

   public String getPrevious(int i) {
      return subject.get(i).getPrevious();
   }

   // i번째노드의 j번째 선수과목 node를 반환
   public Node getPrev(int i, int j) {
      return subject.get(i).getPrev(j);
   }

   // 대부분의 선수과목 node는 하나이기때문에 사용할때 편하게 overloading함
   public Node getPrev(int i) {
      return subject.get(i).getPrev(0);
   }

   // 학년을 인자로 넣어주면 그 학년에 해당하는 노드로 이루어진 ArrayList가 나옴
   public ArrayList getNodelist(int grade) {

      if (grade > 4) {
         System.out.println("학년을 제대로 치세요.");
         return null;
      }

      ArrayList<Node> nodelist = new ArrayList();
      int num = 0;

      for (int i = 0; i < subject.size(); i++) {

         num = Integer.parseInt(subject.get(i).getDepth());
         if (num == grade) {
            nodelist.add(subject.get(i));
         }
      }

      return nodelist;
   }

   // setter
   // i번째 노드의 prevnode를 prev로 설정
   public void setPrev(int i, Node prev) {
      subject.get(i).addPrev(prev);
   }
   
   public void initPrevCount(int i){
      subject.get(i).initCount();
   }
   // 나머지 setter들은 많은 필요 없다고 판단
   // 필요하다고 생각된다면 알아서 만들기

}