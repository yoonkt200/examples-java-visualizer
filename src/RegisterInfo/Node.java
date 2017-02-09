package RegisterInfo;

import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import javax.imageio.ImageIO;

import processing.core.PApplet;
import processing.core.PImage;

public class Node {
   
   private static File path = new File("");
   
   private String depth; // 학년
   private String classification; // 교과구분
   private String name; // 과목이름
   private String grade; // 학점
   private String time; // 시간
   private String previous; // 선수과목의 이름
   private String term;
   private ArrayList<Node> prev = new ArrayList<Node>(); // 선수과목 node
   private int prevCount = 0;

   // 원의 draw에 필요한 기본정보들
   private float x;
   private float y;
   private float dX;
   private float dY;
   private float angle; // 원의 뻗어나가는 방향성 때문에 각도를 일회성으로 저장해야함.
   
   // 과목마다의 고유한 특징을 나타내는 정보들
   Image image = null;
   private int red; // 컬러는 교과구분에 따라 다름
   private int green;
   private int blue;
   private float radius; // 원의 반지름. ((학점+시간)/2)*customSize
   private float radius_rate = 6;
   private final float random_angle = PApplet.radians(((float) Math.random() - (float) Math.random()) * 5);
   private final float random_easing_speed = (float) Math.random() * 8 + 3;
   // 2가지 랜덤값 : 자연스러운 움직임을 위한것

   private Node nextNode; // 역방향 easing에 관련된 정보들
   private float ndX = 0;
   private float ndY = 0;
   private boolean reverse = false;
   
   // onMouse 일때의 상태
   private boolean highlightStatus = false;

   // constructor
   public Node() {
      depth = null;
      classification = null;
      name = null;
      grade = null;
      time = null;
      previous = null;
      term = null;
   }

   public Node(String depth, String classification, String name, String grade, String time, String previous, String term) {
      radius = ((Float.parseFloat(grade) + Float.parseFloat(time)) / 2 + 1) * radius_rate;

      this.depth = depth;
      this.classification = classification;
      this.name = name;
      this.grade = grade;
      this.time = time;
      this.previous = previous;
      this.term = term;
      setRGB();
      setImage();
   }

   // setter
   public void initCount(){
      prevCount = 0;
   }
   
   public void setHightlight(boolean hightlight){
      this.highlightStatus = hightlight;
   }
   
   public void setReverse(boolean reverse) {
      this.reverse = reverse;
   }

   public void setNDX(float ndx) {
      this.ndX = ndx;
   }

   public void setNDY(float ndy) {
      this.ndY = ndy;
   }

   
   
   public void setNextNode(Node node) {
      this.nextNode = node;
   }

   public void setDX(float dX) {
      this.dX = dX;
   }

   public void setDY(float dY) {
      this.dY = dY;
   }

   private void setRGB() {
      if (this.classification.equals("대학필수")) {
         red = 193;
         blue = 72;
         green = 55;
      } else if (this.classification.equals("기초과목")) {
         red = 97;
         blue = 79;
         green = 84;
      } else if (this.classification.equals("전공필수")) {
         red = 54;
         blue = 138;
         green = 125;
      } else if (this.classification.equals("전공선택")) {
         red = 224;
         blue = 60;
         green = 136;
      } else {
         red = 56;
         blue = 40;
         green = 44;
      }
   }

   public void setAngle(float angle) {
      this.angle = angle;
   }

   private void setImage() {
      String filename = name + ".jpg";

      try {
         // Read from a file
         File sourceimage = new File(path.getAbsolutePath() + "\\image\\" + filename);
         image = ImageIO.read(sourceimage);
      } catch (IOException e) {
         image = null;
      }
   }

   public void addCount(){
      prevCount++;
   }
   
   public int getCount(){
      return prevCount;
   }
   
   public void setRadius(float radius) {
      this.radius = radius;
   }

   public void setDepth(String depth) {
      this.depth = depth;
   }

   public void setClassification(String classification) {
      this.classification = classification;
   }

   public void setName(String name) {
      this.name = name;
   }

   public void setGrade(String grade) {
      this.grade = grade;
   }

   public void setTime(String time) {
      this.time = time;
   }

   public void setPrevious(String previous) {
      this.previous = previous;
   }

   public void addPrev(Node Prev) {
      this.prev.add(Prev);
   }

   // getter
   public boolean isHighlight(){
      return highlightStatus;
   }
   
   public boolean isReverse() {
      return reverse;
   }

   public String getTerm(){
      return term;
   }
   
   public float getNDX() {
      return ndX;
   }

   public float getNDY() {
      return ndY;
   }

   public Image getImage() {
      return image;
   }

   public Node getNextNode() {
      return nextNode;
   }

   public float getSpeed() {
      return random_easing_speed;
   }

   public float getDX() {
      return dX;
   }

   public float getDY() {
      return dY;
   }

   public float getRandomAngle() {
      return random_angle;
   }

   public float getAngle() {
      return angle;
   }

   public float getRadius() {
      return radius;
   }

   public int getRed() {
      return red;
   }

   public int getGreen() {
      return green;
   }

   public int getBlue() {
      return blue;
   }

   public String getDepth() {
      return depth;
   }

   public String getClassification() {
      return classification;
   }

   public String getName() {
      return name;
   }

   public String getGrade() {
      return grade;
   }

   public String getTime() {
      return time;
   }

   public String getPrevious() {
      return previous;
   }

   public Node getPrev(int i) {
      return prev.get(i);
   }

   public int numOfPrev() {
      return prev.size();
   }

   public void setX(float x) {
      this.x = x;
   }

   public void setY(float y) {
      this.y = y;
   }

   public float getX() {
      return x;
   }

   public float getY() {
      return y;
   }

}