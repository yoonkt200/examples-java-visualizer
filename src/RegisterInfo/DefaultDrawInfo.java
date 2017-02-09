
package RegisterInfo;

import java.util.ArrayList;

import processing.core.PApplet;

public class DefaultDrawInfo extends PApplet {

	float first_year_angle;
	float second_year_angle;
	float third_year_angle;
	float fourth_year_angle;
	float length;

	public DefaultDrawInfo(float first_year_angle, float second_year_angle, float third_year_angle,
			float fourth_year_angle, float length) {
		
		this.first_year_angle = first_year_angle;
		this.second_year_angle = second_year_angle;
		this.third_year_angle = third_year_angle;
		this.fourth_year_angle = fourth_year_angle;
		this.length = length;

	}

	public void drawDefault(ArrayList<Node> first_year, ArrayList<Node> second_year, ArrayList<Node> third_year,
			ArrayList<Node> fourth_year, Node head) {

		drawDefault_Depth(first_year, head, 1);
		drawDefault_Depth(second_year, head, 2);
		drawDefault_Depth(third_year, head, 3);
		drawDefault_Depth(fourth_year, head, 4);
		
	}

	private void drawDefault_Depth(ArrayList<Node> arrlist, Node head, int depth) {

		for (int i = 0; i < arrlist.size(); i++) {
			if (arrlist.get(i).getPrevious().equals("None")) {
				default_update(arrlist.get(i), head, length * depth, i, Integer.toString(depth));
			}

			else {
				if (arrlist.get(i).numOfPrev() == 1) {
					if (arrlist.get(i).getDepth().equals(arrlist.get(i).getPrev(0).getDepth())) {
						exceptionDrawing(arrlist.get(i), arrlist.get(i).getPrev(0), head);
					}

					else {
						int diff = depth - Integer.parseInt(arrlist.get(i).getPrev(0).getDepth());
						if (diff == 0) {
							diff = 1;
						}
						default_update(arrlist.get(i), arrlist.get(i).getPrev(0), length * diff, i,
								Integer.toString(depth));

					}
				} else {
					defaultdrawMultiplePrev(arrlist.get(i), head);
				}
			}
		}
	}

	private void default_update(Node temp, Node prev, float length, int idx, String depth) {
		switch (depth) {

		case "1": {
			setDefault(temp, prev, idx, first_year_angle, length);
			break;
		}

		case "2": {
			setDefault(temp, prev, idx, second_year_angle, length);
			break;
		}

		case "3": {
			setDefault(temp, prev, idx, third_year_angle, length);
			break;
		}

		case "4": {
			setDefault(temp, prev, idx, fourth_year_angle, length);
			break;
		}

		default:
			break;
		}
	}

	private void setDefault(Node temp, Node prev, int idx, float angle, float length) {
		if (temp.getPrevious().equals("None")) {
			temp.setAngle(angle * (idx + 1) + temp.getRandomAngle());
			temp.setX((length * cos(temp.getAngle())) + (prev.getX()));
			temp.setY((length * sin(temp.getAngle())) + (prev.getY()));
			temp.setDX(prev.getX() - temp.getX());
			temp.setDY(prev.getY() - temp.getY());
		}

		else {
			if(prev.getCount()>=8){
				length = (float) (length * 1.2);
			}
			float angle1 = (float) (prev.getAngle() + Math.pow(-1, prev.getCount()) * radians(15 * (((prev.getCount() + 1) / 2))));
			temp.setAngle(angle1);
			temp.setX((length * cos(temp.getAngle())) + (prev.getX()));
			temp.setY((length * sin(temp.getAngle())) + (prev.getY()));
			temp.setDX(prev.getX() - temp.getX());
			temp.setDY(prev.getY() - temp.getY());
			prev.addCount();
		}
	}

	private void defaultdrawMultiplePrev(Node temp, Node head) {
		float xx = (temp.getPrev(0).getX() + temp.getPrev(1).getX());
		float yy = (temp.getPrev(0).getY() + temp.getPrev(1).getY());

		float angle = atan((abs(head.getX() - xx)) / (head.getY() - yy)) + temp.getRandomAngle();

		temp.setAngle(angle);
		temp.setX(Integer.parseInt(temp.getDepth()) * length * cos(temp.getAngle()) + head.getX());
		temp.setY(Integer.parseInt(temp.getDepth()) * length * sin(temp.getAngle()) + head.getY());
		temp.setDX(temp.getPrev(0).getX() - temp.getX());
		temp.setDY(temp.getPrev(0).getY() - temp.getY());
		// index가 0이나 1이나 어차피 prev노드를 따라 easing 하기 때문에 임의로 0을 넣음
	}

	private void exceptionDrawing(Node temp, Node prev, Node head) {
		float depth = Float.parseFloat(temp.getDepth()); 
		float angle = (float) (prev.getAngle() + 2 * Math.asin(1.0/(2*depth)));

		temp.setAngle(angle);
		temp.setX(Integer.parseInt(temp.getDepth()) * length * cos(temp.getAngle()) + head.getX());
		temp.setY(Integer.parseInt(temp.getDepth()) * length * sin(temp.getAngle()) + head.getY());
		temp.setDX(temp.getPrev(0).getX() - temp.getX());
		temp.setDY(temp.getPrev(0).getY() - temp.getY());

	}
}