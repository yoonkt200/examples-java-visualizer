package RegisterInfo;

import java.util.ArrayList;

import javax.imageio.ImageIO;

import processing.core.PApplet;
import processing.core.PFont;
import processing.core.PImage;
import java.awt.Toolkit;
import java.io.EOFException;
import java.io.File;
import java.io.IOException;
import java.awt.Dimension;
import java.awt.Image;

public class InfoSketch extends PApplet {

	private File path = new File("");

	private static DataImport socialmedia;
	private static DataImport smartcontents;
	private static DataBase registerdb = new DataBase();
	private static ImageHelper image = new ImageHelper();

	private DefaultDrawInfo defalutdrawinfo;

	private Node head;

	ArrayList<Node> first_year = new ArrayList();
	ArrayList<Node> second_year = new ArrayList();
	ArrayList<Node> third_year = new ArrayList();
	ArrayList<Node> fourth_year = new ArrayList();

	private float first_year_angle;
	private float second_year_angle;
	private float third_year_angle;
	private float fourth_year_angle;

	private int subject_switch = 0;
	private boolean class_switch1 = true;
	private boolean class_switch2 = true;
	private boolean class_switch3 = true;
	private boolean class_switch4 = true;
	private boolean touch = false;
	private ArrayList<Node> onMouseNode = new ArrayList();

	Dimension res = Toolkit.getDefaultToolkit().getScreenSize();
	private double screen_rate = 1;
	private float CenterX_rate = (float) 0.42;
	private float CenterY_rate = (float) 0.5;
	private float length_rate = (float) 0.09;
	private float screen_width = (float) (res.width * screen_rate);
	private float screen_height = (float) (res.height * screen_rate);
	double infoWidth = screen_width * (1.0 / 5.0);
	double infoHeight = screen_height * (1.0 / 3.0);
	private final float centerX = screen_width * CenterX_rate; // 헤드노드의 초기값
	private final float centerY = screen_height * CenterY_rate; // 헤드노드의 초기값
	private final float length = screen_height * length_rate; // 원 사이 연결하는 길이
	private final float easing = (float) 0.02;
	private final float hide_transparency = 40;

	private float buttonStartX = (float) (screen_width * (0.025));
	private float buttonStartY = (float) (screen_height * (0.04));

	private float checkStart1 = (float) (screen_height * (0.2));
	private float checkStart2 = checkStart1 + (float) (screen_height * (0.035));
	private float checkStart3 = checkStart2 + (float) (screen_height * (0.035));
	private float checkStart4 = checkStart3 + (float) (screen_height * (0.035));
	private float checkStartX = (float) (screen_width * (0.025));
	private float checkHeight = (float) (screen_width * (1.0 / 70.0));

	private int fontSize = (int) (screen_width * (0.0109));
	private PFont font;

	private PImage background = image.background;
	private PImage check;
	private PImage checkBox;
	private PImage checkBox_onMouse;
	private PImage tab;
	private PImage tab_onMouse;

	public void settings() {
		size((int) screen_width, (int) screen_height);
		smooth();

		if (subject_switch == 1) {
			registerdb = socialmedia.read();
			tab = image.button_social;
			tab_onMouse = image.button_social_onMouse;
			subject_switch = 0;
		} else if (subject_switch == 2) {
			registerdb = smartcontents.read();
			subject_switch = 0;
			tab = image.button_smart;
			tab_onMouse = image.button_smart_onMouse;
		} else {
			registerdb = socialmedia.read();
			subject_switch = 0;
			tab = image.button_social;
			tab_onMouse = image.button_social_onMouse;
		}

		background.resize(width, height);

		setInformation(registerdb);
	}

	public void setup() {
		font = createFont(path.getAbsolutePath() + "\\font\\" + "08SeoulNamsanM_0.ttf", fontSize);
		textFont(font);
	}

	public void setInformation(DataBase data) {
		// 헤드정보 설정
		head = data.getHeadNode();
		head.setX(centerX);
		head.setY(centerY);
		head.setRadius(80);

		first_year = data.getNodelist(1);
		second_year = data.getNodelist(2);
		third_year = data.getNodelist(3);
		fourth_year = data.getNodelist(4);

		// 각도를 미리 계산
		first_year_angle = radians(360 / first_year.size());
		second_year_angle = radians(360 / second_year.size());
		third_year_angle = radians(360 / third_year.size());
		fourth_year_angle = radians(360 / fourth_year.size());

		defalutdrawinfo = new DefaultDrawInfo(first_year_angle, second_year_angle, third_year_angle, fourth_year_angle,
				length);
		defalutdrawinfo.drawDefault(first_year, second_year, third_year, fourth_year, head);

	}

	public void draw() {
		background(background);

		if (subject_switch != 0) {
			settings();
		}

		touch = false;
		if (touch == false) {
			checkTouched(head);
		}

		easingHeadNodePoint();

		hang_Classification(fourth_year);
		hang_Classification(third_year);
		hang_Classification(second_year);
		hang_Classification(first_year);

		drawButton(buttonStartX, buttonStartY);

		if (head.isHighlight()) {
			drawCircle_change(head);
		} else {
			drawCircle(head); // headNode 그림
		}

		checkbox();
	}

	private void checkbox() {
		drawCheckBox(checkStartX, checkStart1, class_switch1, 1);
		drawCheckBox(checkStartX, checkStart2, class_switch2, 2);
		drawCheckBox(checkStartX, checkStart3, class_switch3, 3);
		drawCheckBox(checkStartX, checkStart4, class_switch4, 4);

		drawText("기초과목", checkStartX + checkHeight + 15, checkStart1 + checkHeight - 3, 70);
		drawText("대학필수", checkStartX + checkHeight + 15, checkStart2 + checkHeight - 3, 70);
		drawText("전공선택", checkStartX + checkHeight + 15, checkStart3 + checkHeight - 3, 70);
		drawText("전공필수", checkStartX + checkHeight + 15, checkStart4 + checkHeight - 3, 70);

	}

	private void hang_Classification(ArrayList<Node> arrlist) {

		for (int i = 0; i < arrlist.size(); i++) {
			if (arrlist.get(i).numOfPrev() != 0) {
				hang_update(arrlist.get(i), arrlist.get(i).getPrev(0));
			}

			else {
				drawMultiplePrev(arrlist.get(i));
			}
		}
	}

	private void hang_update(Node temp, Node prev) {

		if (temp.isReverse() && temp.getNextNode() != null) {
			easingReverseNodePoint(temp, temp.getNextNode());
		} else {
			easingNodePoint(temp, prev);
		}

		if (class_switch1 == true) {
			if (temp.getClassification().equals("기초과목")) {
				drawLine(temp, prev);
				drawCircle(temp);
			}
		} else {
			if (temp.getClassification().equals("기초과목")) {
				drawLine_Transparent(temp, prev);
				drawCircle_Transparent(temp);
			}
		}

		if (class_switch2 == true) {
			if (temp.getClassification().equals("대학필수")) {
				drawLine(temp, prev);
				drawCircle(temp);
			}
		} else {
			if (temp.getClassification().equals("대학필수")) {
				drawLine_Transparent(temp, prev);
				drawCircle_Transparent(temp);
			}
		}

		if (class_switch3 == true) {
			if (temp.getClassification().equals("전공선택")) {
				drawLine(temp, prev);
				drawCircle(temp);
			}
		} else {
			if (temp.getClassification().equals("전공선택")) {
				drawLine_Transparent(temp, prev);
				drawCircle_Transparent(temp);
			}
		}

		if (class_switch4 == true) {
			if (temp.getClassification().equals("전공필수")) {
				drawLine(temp, prev);
				drawCircle(temp);
			}
		} else {
			if (temp.getClassification().equals("전공필수")) {
				drawLine_Transparent(temp, prev);
				drawCircle_Transparent(temp);
			}
		}

		if (touch == false) {
			setInitialization(temp);
			temp.setHightlight(false);
			checkTouched(temp);
		}
	}

	private void change_Classification() {
		if (mouseY >= checkStart1 && mouseY <= checkStart1 + checkHeight && mouseX >= checkStartX
				&& mouseX <= checkStartX + checkHeight) {
			if (class_switch1 == true) {
				class_switch1 = false;
			} else {
				class_switch1 = true;
			}
		} else if (mouseY >= checkStart2 && mouseY <= checkStart2 + checkHeight && mouseX >= checkStartX
				&& mouseX <= checkStartX + checkHeight) {
			if (class_switch2 == true) {
				class_switch2 = false;
			} else {
				class_switch2 = true;
			}
		} else if (mouseY >= checkStart3 && mouseY <= checkStart3 + checkHeight && mouseX >= checkStartX
				&& mouseX <= checkStartX + checkHeight) {
			if (class_switch3 == true) {
				class_switch3 = false;
			} else {
				class_switch3 = true;
			}
		} else if (mouseY >= checkStart4 && mouseY <= checkStart4 + checkHeight && mouseX >= checkStartX
				&& mouseX <= checkStartX + checkHeight) {
			if (class_switch4 == true) {
				class_switch4 = false;
			} else {
				class_switch4 = true;
			}
		}

	}

	private void change_SubjectSwitch() { // 학과 view 변경
		if (mouseX >= buttonStartX && mouseX <= buttonStartX + image.button_smart.width / 2 && mouseY >= buttonStartY
				&& mouseY <= buttonStartY + image.button_smart.height) {
			subject_switch = 1;
		}

		else if (mouseX >= buttonStartX + image.button_smart.width / 2
				&& mouseX <= buttonStartX + image.button_smart.width && mouseY >= buttonStartY
				&& mouseY <= buttonStartY + image.button_smart.height) {
			subject_switch = 2;
		}
	}

	private void drawText(Node temp) { // 노드의 Text

		fill(70);
		textSize(fontSize - 2);
		text(temp.getName(), temp.getX() - 45, temp.getY() - temp.getRadius() / 2 - fontSize / 2);
	}

	private void drawText_change(Node temp) {

		fill(0);
		textSize(fontSize + 5);
		text(temp.getName(), temp.getX() - 40, temp.getY() - temp.getRadius() / 2 - fontSize);
	}

	private void drawText(String text, float x, float y, int color) { // 버튼의
		// text

		fill(color);
		textSize(fontSize);
		text(text, x, y);
	}

	private void drawText_Transparent(Node temp) { // 체크된 상태의 노드들의 Text

		textSize(fontSize);
		fill(255, hide_transparency);
		text(temp.getName(), temp.getX() - 45, temp.getY() - temp.getRadius() / 2 - 10);
	}

	private void drawCheckBox(float startX, float startY, boolean class_switch, int num) {
		int R = 0, G = 0, B = 0;

		switch (num) {
		case 1: {
			R = 97;
			G = 84;
			B = 79;
			check = image.check1;
			break;

		}

		case 2: {
			R = 193;
			G = 55;
			B = 72;
			check = image.check2;
			break;
		}

		case 3: {
			R = 224;
			G = 136;
			B = 60;
			check = image.check3;
			break;
		}

		case 4: {
			R = 54;
			G = 125;
			B = 138;
			check = image.check4;
			break;
		}

		default: {
			break;
		}
		}

		if (mouseX >= startX && mouseX <= startX + checkHeight && mouseY >= startY && mouseY <= startY + checkHeight) {
			if (class_switch == false) {
				noStroke();
				fill(R - 30, G - 30, B - 30, 255);
				rect(startX, startY, checkHeight, checkHeight);

			} else {
				noStroke();
				fill(R - 30, G - 30, B - 30, 255);
				rect(startX, startY, checkHeight, checkHeight);
				image(check, startX + checkHeight / 5, startY - image.check1.height/2);
			}
		}

		else {
			if (class_switch == false) {
				noStroke();
				fill(R, G, B, 255);
				rect(startX, startY, checkHeight, checkHeight);
			} else {
				noStroke();
				fill(R, G, B, 255);
				rect(startX, startY, checkHeight, checkHeight);
				image(check, startX + checkHeight / 5, startY - image.check1.height/2);
			}
		}
	}

	private void drawButton(float startX, float startY) {

		if (mouseX >= startX && mouseX <= startX + image.button_smart.width && mouseY >= image.button_smart.height
				&& mouseY <= startY + image.button_smart.height) {
			image(tab_onMouse, startY, startY);
		}

		else {
			image(tab, startY, startY);
		}
	}

	private void drawLine(Node temp, Node prev) { // 두 원을 연결
		if (temp.isHighlight()) {
			return;
		}
		strokeCap(ROUND);
		strokeWeight(1);
		stroke(128, 129, 128, 100);
		line(temp.getX(), temp.getY(), prev.getX(), prev.getY());
	}

	private void drawLine_change(Node temp, Node prev) { // 두 원을 연결
		strokeCap(ROUND);
		strokeWeight(5);
		stroke(56, 44, 40);
		line(temp.getX(), temp.getY(), prev.getX(), prev.getY());
		strokeWeight(1);
	}

	private void drawLine_Transparent(Node temp, Node prev) { // 두 원을 연결
		if (temp.isHighlight()) {
			return;
		}
		strokeCap(ROUND);
		strokeWeight(2);
		stroke(255, hide_transparency);
		line(temp.getX(), temp.getY(), prev.getX(), prev.getY());
	}

	private void drawCircle(Node temp) { // update에서 temp 노드만 circle을 그려줌
		if (temp.isHighlight()) {
			return;
		}
		if (temp == head) {
			noStroke();
			fill(temp.getRed(), temp.getGreen(), temp.getBlue());
			ellipse(temp.getX(), temp.getY(), temp.getRadius(), temp.getRadius());

			noFill();
			stroke(temp.getRed(), temp.getGreen(), temp.getBlue());
			ellipse(temp.getX(), temp.getY(), temp.getRadius() + temp.getRadius() * (float) 0.1,
					temp.getRadius() + temp.getRadius() * (float) 0.1);

		} else {
			noStroke();
			fill(temp.getRed(), temp.getGreen(), temp.getBlue(), 220);
			ellipse(temp.getX(), temp.getY(), temp.getRadius(), temp.getRadius());
			drawText(temp);

			noFill();
			stroke(temp.getRed(), temp.getGreen(), temp.getBlue());
			ellipse(temp.getX(), temp.getY(), temp.getRadius() + temp.getRadius() * (float) 0.25,
					temp.getRadius() + temp.getRadius() * (float) 0.25);
		}
	}

	private void drawCircle_change(Node temp) { // update에서 temp 노드만 circle을 그려줌
		temp.setHightlight(true); // on change 인 노드는 hightlight 중인 노드임
		fill(56, 44, 40);
		noStroke();
		ellipse(temp.getX(), temp.getY(), temp.getRadius() + temp.getRadius() * (float) 0.25,
				temp.getRadius() + temp.getRadius() * (float) 0.25);
		drawText_change(temp);
		temp.setReverse(true); // circle change 되는 노드는 역방향으로 easing되어야 하는 노드임
	}

	private void drawCircle_Transparent(Node temp) {
		if (temp.isHighlight()) {
			return;
		}
		drawText_Transparent(temp);
		strokeWeight(5);
		stroke(temp.getRed(), temp.getGreen(), temp.getBlue(), hide_transparency - 10);
		fill(temp.getRed(), temp.getGreen(), temp.getBlue(), hide_transparency - 10);
		ellipse(temp.getX(), temp.getY(), temp.getRadius(), temp.getRadius());
	}

	private void drawPrev_change(Node temp) { // recursive 하게 highlight 궤도 추적
		drawCircle_change(temp);

		if (temp.numOfPrev() == 0) {
			return;
		}

		else {
			for (int i = 0; i < temp.numOfPrev(); i++) {
				drawPrev_change(temp.getPrev(i));
			}
			drawCircle_change(temp.getPrev(0));
			drawLine_change(temp, temp.getPrev(0));
		}
	}

	private void drawMultiplePrev(Node temp) { // 선수강과목이 2개인 경우, draw에서 예외로
		// 판단하여 이곳으로 보냄
		for (int i = 0; i < temp.numOfPrev(); i++) {
			drawLine(temp, temp.getPrev(i));
		}
		drawCircle(temp);
		checkTouched(temp);
	}

	private void checkTouched(Node temp) { // 마우스 커서가 노드 위에 올라갔는지 판단
		if (dist(mouseX, mouseY, temp.getX(), temp.getY()) <= temp.getRadius() / 2) {
			onMouseNode.add(temp);

			if (onMouseNode.size() > 1) {
				onMouseNode.remove(1);
			}
			touch = true;
			activateStatus(temp);
		}

		else {
			if (mousePressed == false) {
				onMouseNode.clear();
			}
			head.setHightlight(false);
			touch = false;
		}
	}

	private void activateStatus(Node temp) { // touch 일때 활성화되는 상태들
		if (onMouseNode.size() > 0) {
			temp = onMouseNode.get(0);
			uploadImage(temp);
		}

		if (temp.getDepth().equals("0")) {
			drawCircle_change(temp);
		}

		else {
			drawCircle_change(temp);
			drawLine_change(temp, temp.getPrev(0));
			if (temp.numOfPrev() >= 1) {
				for (int i = 0; i < temp.numOfPrev(); i++) {
					drawPrev_change(temp.getPrev(i));
				}
			}
		}
	}

	private void easingNodePoint(Node temp, Node prev) { // 노드를 마우스로 당기고 난 후에 원래
		// 자리로 돌아가게 하는 역할
		if (onMouseNode.size() > 0) {
			if (onMouseNode.get(0) == temp) {
				return;
			}
		}
		temp.setX(temp.getX() + ((prev.getX() + temp.getDX() - temp.getX()) * easing * temp.getSpeed()));
		temp.setY(temp.getY() + ((prev.getY() + temp.getDY() - temp.getY()) * easing * temp.getSpeed()));
	}

	private void easingReverseNodePoint(Node temp, Node next) { // 선수강노드를 당기는
		// 방향으로 딸려오게 함
		if (onMouseNode.size() > 0) {
			if (onMouseNode.get(0) == temp) {
				return;
			}
		}
		temp.setX(temp.getX() + ((next.getX() + temp.getNDX() - temp.getX()) * easing * temp.getSpeed()));
		temp.setY(temp.getY() + ((next.getY() + temp.getNDY() - temp.getY()) * easing * temp.getSpeed()));
	}

	private void easingHeadNodePoint() { // 헤드노드를 easing
		head.setX(head.getX() + ((centerX - head.getX()) * easing));
		head.setY(head.getY() + ((centerY - head.getY()) * easing));
	}

	private void uploadImage(Node temp) { // 과목정보 upload
		try {
			PImage img = new PImage(temp.getImage());
			img.resize((int) infoWidth, (int) infoHeight);
			image(img, width - img.width, 0);
		} catch (Exception e) {
		}
	}

	private void setReverseInfo(Node temp, Node next) { // reverse_easing 을 위해
		// reverse정보 set
		temp.setReverse(true);
		temp.setNextNode(next);
		temp.setNDX(temp.getX() - next.getX());
		temp.setNDY(temp.getY() - next.getY());

		if (temp.numOfPrev() == 0) {
			return;
		}

		else {
			for (int i = 0; i < temp.numOfPrev(); i++) {
				setReverseInfo(temp.getPrev(i), temp);
			}
		}
	}

	private void setInitialization(Node temp) { // non reverse_easing 으로 돌아가기 위해
		// 초기화
		temp.setReverse(false);
		temp.setNDX(0);
		temp.setNDY(0);
		temp.setNextNode(null);
	}

	public void mouseDragged() { // locked는 드래그시 버튼 비활성화 해줌
		if (onMouseNode.size() > 0) {
			onMouseNode.get(0).setX(mouseX);
			onMouseNode.get(0).setY(mouseY);
		}
	}

	public void mousePressed() { // 노드를 클릭했을 경우 reverse 정보 setting
		// 체크박스를 클릭했을 경우 체크 on/off

		if (onMouseNode.size() > 0) {
			if (onMouseNode.get(0).getDepth().equals("0")) {
				return;
			}

			else {
				if (onMouseNode.get(0).numOfPrev() >= 1) {
					for (int i = 0; i < onMouseNode.get(0).numOfPrev(); i++) {
						setReverseInfo(onMouseNode.get(0).getPrev(i), onMouseNode.get(0));
					}
				}
			}
		}
	}

	public void mouseClicked() {
		change_SubjectSwitch();
		change_Classification();
	}

	public static void main(String args[]) {
		socialmedia = new DataImport("subject.txt", "소셜미디어 학과", 66);
		smartcontents = new DataImport("subject2.txt", "미디어콘텐츠 학과", 81);
		PApplet.main(new String[] { "--bgcolor=#000000", "RegisterInfo.InfoSketch" });

	}
}