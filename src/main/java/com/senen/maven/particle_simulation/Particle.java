package com.senen.maven.particle_simulation;

import processing.core.PVector;

public class Particle {

	PVector pos, vel, acc;
	int size, m, color;
	float r;

	Particle(float x, float y, float vx, float vy, int m, int size, int color) {
		pos = new PVector(x, y);
		vel = new PVector(vx, vy);
		acc = new PVector(0, 0);
		this.m = m;
		this.size = size;
		this.r = size * 0.5f;
		this.color = color;
	}


	void update(float dt) {
		vel.x += acc.x * dt;
		vel.y += acc.y * dt;
		pos.x += vel.x * dt;
		pos.y += vel.y * dt;
	}
	
	void solveWidthOverlap(int w) {
		float rightOverlap = pos.x + r - w;
		float leftOverlap = r - pos.x;
		pos.x = rightOverlap > 0 ? pos.x - rightOverlap : pos.x + leftOverlap;
	}
	
	void solveHeightOverlap(int h) {
		float downOverlap = (pos.y + r) - h;
		float upOverlap = r - pos.y;
		pos.y = downOverlap > 0 ? pos.y - downOverlap : pos.y + upOverlap;
	}
	
	void checkEdgeCollision(int width, int height) {
		if (pos.x - r <= 0 || pos.x + r >= width) {
			solveWidthOverlap(width);
			vel.x = -vel.x;
		}
		if (pos.y - r <= 0 || pos.y + r >= height) {
			solveHeightOverlap(height);
			vel.y = -vel.y;
		}
	}

}
