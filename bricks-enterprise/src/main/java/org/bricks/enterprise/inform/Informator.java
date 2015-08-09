package org.bricks.enterprise.inform;

import java.util.LinkedList;

import org.bircks.enterprise.control.panel.Skinner;
import org.bricks.utils.Quarantine;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.Viewport;

public class Informator {
	
	private static final String delimiter = "\n";

	private Stage stage;
	private Stack stack;
	private Table table;
	private Label label;
	
	
	private Quarantine<String> logInput = new Quarantine<String>(5);
	private LinkedList<String> log = new LinkedList<String>();
	private int logSize = 7, curLogSize;
	private StringBuffer buffer = new StringBuffer();
	
	private static Informator informator;

	public static void init(){
		informator = new Informator();
	}
	
	private Informator(){
		stage = new Stage();
		stack = new Stack();
		label = new Label("", Skinner.instance().skin(), "default");
		label.setAlignment(Align.left);
		table = new Table();
//		table.setDebug(true);
		table.left().pad(5).top().add(label);
		stack.add(table);
		stage.addActor(stack);
	}
	
	public void logData(String data){
		logInput.push(data);
	}
	
	public static void log(String data){
		informator.logData(data);
	}
	
	private void updateLogData(){
		boolean update = false;
		for(String newData : logInput){
			update = true;
			log.addFirst(newData);
			if(curLogSize == logSize){
				log.removeLast();
			}else{
				++curLogSize;
			}
		}
		if(update){
			buffer.delete(0, buffer.length());
			for(String s : log){
				buffer.append(s).append(delimiter);
			}
			label.setText(buffer);
		}
	}
	
	public void renderData(Camera camera){
		updateLogData();
		stage.draw();
	}
	
	public static void render(Camera camera){
		informator.renderData(camera);
	}
	
	public void resizeViewport(int width, int height) {
		Viewport viewport = stage.getViewport();
		viewport.setWorldWidth(width);
		viewport.setWorldHeight(height);
		viewport.update(width,  height, true);
		stack.setHeight(height / 3);
		table.setHeight(height / 3);
		table.setWidth(width);
		label.setWidth(width);
		table.setPosition(0, 0);
		stack.setOrigin(0f, 0f);
		stack.setPosition(0, height * 2 / 3);
	}
	
	public static void resize(int width, int height){
		informator.resizeViewport(width, height);
	}
}
