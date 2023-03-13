package com.senen.maven.particle_simulation;

import java.util.ArrayList;
import processing.core.PApplet;

public class Simulation extends PApplet {

	ArrayList<Particle> particles;
	boolean fullscreen, pause;
	int w, h, bg, prevFrame;
	float dt; // Delta time (change in time)
	int tileSize, nCols, nRows; // partition optimization stuff
	Preset preset;

	public Simulation(int w, int h, int bg, int tileSize, Preset preset) {
		this.w = w;
		this.h = h;
		this.bg = bg;
		this.tileSize = tileSize;
		this.preset = preset;
		particles = new ArrayList<>();
		fullscreen = false;
	}

	public Simulation(boolean fullscreen, int bg, int tileSize, Preset preset) {
		this.fullscreen = fullscreen;
		this.tileSize = tileSize;
		this.bg = bg;
		this.preset = preset;
		particles = new ArrayList<>();
	}
	
	enum Preset {
		FIRST,
		SECOND,
		THIRD,
	}

	@Override
	public void settings() {
		if (fullscreen) fullScreen();
		else if (w != 0 || h != 0) size(w, h);
		else size(1200, 600);
	}


	@Override
	public void keyPressed() {
		if (key == ' ') pause = !pause;
		if (pause) noLoop();
		else loop();
	}	


	@Override
	public void setup() {
		surface.setResizable(false); // Enable double buffering
		colorMode(RGB);
		stroke(255);
		strokeWeight(1);
		fill(255);
		background(bg);
		frameRate(200);
		this.nCols = width / tileSize;
		this.nRows = height / tileSize;
		this.pause = false;

		switch (preset) {
		case FIRST:
			Utils.populate(particles);
			break;
		case SECOND:
			Utils.populate(particles, 100, 100, 20, width, height);
			break;
		case THIRD:
			Utils.populate(particles, 300, -300, 300, 50, 10, width, height);
			break;
		default:
			break;
		}
	}


	@Override
	public void draw() {
		background(0);
		fill(255);
		text((int) frameRate, 10, 20);
		text(frameCount, 10, 40);
		dt = 1 / frameRate;
		
		updatePhysicsSubsteps(dt, 8);
		for (Particle p : particles) {
			stroke(p.color);
			fill(p.color);
			ellipse(p.pos.x, p.pos.y, p.size, p.size);
		}
	}

	
	void updatePhysicsSubsteps(float dt, int substeps) {
		float subDt = dt / Float.valueOf(substeps);
		for (int i = substeps; i > 0; i--) 
			updatePhysics(subDt);
	}


	void updatePhysics(float dt) {
		for (Particle p : particles) {
			p.update(dt);
			p.checkEdgeCollision(width, height);
			for (Particle p2 : particles) 
				if (p != p2) 
					smartCollision(p, p2);
		}
	}


	void smartCollision(Particle p1, Particle p2) {
		int col1R= (int) floor((p1.pos.x + p1.r) / tileSize); 
		int col1L = (int) floor((p1.pos.x - p1.r) / tileSize); 
		int row1U = (int) floor((p1.pos.y + p1.r) / tileSize); 
		int row1D = (int) floor((p1.pos.y - p1.r) / tileSize); 
		int col2R = (int) floor((p2.pos.x + p2.r) / tileSize); 
		int col2L = (int) floor((p2.pos.x - p2.r) / tileSize); 
		int row2U = (int) floor((p2.pos.y + p2.r) / tileSize); 
		int row2D = (int) floor((p2.pos.y - p2.r) / tileSize); 
		boolean sameCol = col1L == col2L || col1R == col2R;
		boolean sameRow = row1U == row2U || row1D == row2D;
		if (sameCol && sameRow && Utils.collide(p1, p2)) {
			Utils.solveOverlap(p1, p2);
			Utils.solveEllasticCollision(p1, p2);
			Utils.solveEllasticCollision(p2, p1);
		} 
	}


	void testMode() {
		if (frameCount == prevFrame + 1) {
			noLoop();
			pause = !pause;
			prevFrame++;
		}
	}


	int rColor() {
		return color(random(255), random(255), random(255));
	}

	
	// UNUSED
	public void smartCollisionTime(Particle p1, Particle p2) {
		int col1R= (int) floor((p1.pos.x + p1.r) / tileSize); 
		int col1L = (int) floor((p1.pos.x - p1.r) / tileSize); 
		int row1U = (int) floor((p1.pos.y + p1.r) / tileSize); 
		int row1D = (int) floor((p1.pos.y - p1.r) / tileSize); 
		int col2R = (int) floor((p2.pos.x + p2.r) / tileSize); 
		int col2L = (int) floor((p2.pos.x - p2.r) / tileSize); 
		int row2U = (int) floor((p2.pos.y + p2.r) / tileSize); 
		int row2D = (int) floor((p2.pos.y - p2.r) / tileSize); 
		boolean sameCol = col1L == col2L || col1R == col2R;
		boolean sameRow = row1U == row2U || row1D == row2D;
		float t = Utils.collisionTime(p1, p2, this.dt);
		if (sameCol && sameRow && t >= 0 && t <= dt) {
			Utils.solveEllasticCollision(p1, p2);
			Utils.solveEllasticCollision(p2, p1);
		} 
	}

	
	public static void main(String[] args) {
		new Simulation(1200, 600, 0, 100, Preset.FIRST).runSketch();
		new Simulation(1200, 600, 0, 100, Preset.SECOND).runSketch();
		new Simulation(1200, 600, 0, 100, Preset.THIRD).runSketch();
	}

}

