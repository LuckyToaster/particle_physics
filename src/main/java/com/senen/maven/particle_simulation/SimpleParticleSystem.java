package com.senen.maven.particle_simulation;

import processing.core.PVector;
import processing.core.PApplet;
import java.util.ArrayList;

/**
* Simple Particle System by Daniel Shiffman.
* 
* Particles are generated each cycle through draw(), fall with gravity, and
* fade out over time. A ParticleSystem object manages a variable size
* (ArrayList) list of particles.
*/
public class SimpleParticleSystem extends PApplet {

	int w, h;
	boolean fullscreen;
	ParticleSystem ps;
	
	public SimpleParticleSystem(int w, int h) {
		this.w = w;
		this.h = h;
	}
	
	public SimpleParticleSystem(boolean fullscreen) {
		this.fullscreen = fullscreen;
	}
	
	@Override
	public void settings() {
		if (fullscreen) fullScreen();
		else size(w, h);
	}

	@Override
	public void setup() {
		ps = new ParticleSystem(new PVector(width / 2, 200));
		surface.setResizable(false); // explicitly enable double buffering 
		frameRate(80);
	}

	@Override
	public void draw() {
		background(0);
		ps.addParticle();
		ps.run();
		fill(255);
		text(frameRate, 20, 20);
	}
	

	public class ParticleSystem {
		ArrayList<Particle> particles;
		PVector origin;

		ParticleSystem(PVector origin) {
			this.origin = origin.copy();
			particles = new ArrayList<>();
		}

		void addParticle() {
			particles.add(new Particle(origin));
		}

		void run() {
			for (int i = particles.size() - 1; i >= 0; i--) {
				Particle p = particles.get(i);
				p.run();
				if (p.isDead()) particles.remove(i);
			}
		}
	}


	public class Particle {
		PVector pos, v, a;
		float 	lifespan;

		Particle(PVector origin) {
			a = new PVector(0, 0.05f);
			v = new PVector(random(-1, 1), random(-2, 0));
			pos = origin.copy();
			lifespan = 255.0f;
		}

		void run() {
			update();
			display();
		}

		void update() {
			v.add(a);
			pos.add(v);
			lifespan -= 1.0;
		}

		void display() {
			stroke(255, lifespan);
			fill(255, lifespan);
			ellipse(pos.x, pos.y, 8, 8);
		}

		boolean isDead() {
			if (lifespan < 0.0) return true;
			else return false;
		}
	}
	

	public static void main(String[] args) {
		new SimpleParticleSystem(true).runSketch();
	}

}
