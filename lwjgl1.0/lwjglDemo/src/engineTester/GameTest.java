package engineTester;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.joml.Vector3f;
import org.lwjgl.opengl.GL11;

import entities.Camera;
import entities.Entity;
import entities.Light;
import entities.Player;
import models.TexturedModel;
import renderEngine.MasterRenderer;
import renderEngine.Window;
import terrains.Terrain;
import textures.TerrainTexture;
import textures.TerrainTexturePack;
import textures.Texture;
import toolbox.Loader;
import toolbox.OBJLoader;

public class GameTest {

	public static void main(String[] args) {

		Window window = new Window("aa", 1280, 720, true);
		Loader loader = new Loader();
		
		//***********************************************//
		TerrainTexture bgTexture = new TerrainTexture("/res/grassy.png");
		TerrainTexture rTexture = new TerrainTexture("/res/dirt.png");
		TerrainTexture gTexture = new TerrainTexture("/res/pinkFlowers.png");
		TerrainTexture bTexture = new TerrainTexture("/res/path.png");
		
		TerrainTexturePack pack = new TerrainTexturePack(bgTexture, rTexture, gTexture, bTexture);
		TerrainTexture blendMap = new TerrainTexture("/res/blendMap.png");
		//***********************************************//
		MasterRenderer msrender = new MasterRenderer(window);
		
		OBJLoader objLoader = new OBJLoader();
		
		List<Entity> entities = new ArrayList<>();
		
		Random rand = new Random();
		
		TexturedModel txmodel = new TexturedModel(objLoader.loadFile("/res/tree.obj"), new Texture("/res/tree.png"));
		TexturedModel txmodel2 = new TexturedModel(objLoader.loadFile("/res/fern.obj"), new Texture("/res/fern.png"));
		TexturedModel txmodel3 = new TexturedModel(objLoader.loadFile("/res/grassModel.obj"), new Texture("/res/grassTexture.png"));
		TexturedModel txmodel4 = new TexturedModel(objLoader.loadFile("/res/lowPolyTree.obj"), new Texture("/res/lowPolyTree.png"));
		for (int i = 0; i < 100; i++) {
			
			Entity entity = new Entity(txmodel, new Vector3f(rand.nextFloat()*1000-600,0,rand.nextFloat()*1000-600), 0,0,0, 4);
			entities.add(entity);
			
			Entity entity2 = new Entity(rand.nextInt(4),txmodel2, new Vector3f(rand.nextFloat()*1000-300,0,rand.nextFloat()*1000-400), 0,0,0, 1);
			entity2.getModel().getTexture().setTransParent(true);
			entity2.getModel().getTexture().setRows(2);
			entities.add(entity2);
			
			Entity entity3 = new Entity(txmodel3, new Vector3f(rand.nextFloat()*1000-400,0,rand.nextFloat()*1000-500), 0,0,0, 1);
			entity3.getModel().getTexture().setTransParent(true);
			entity3.getModel().getTexture().setUseFackLightLing(true);
			entities.add(entity3);
			
			Entity entity4 = new Entity(txmodel4, new Vector3f(rand.nextFloat()*1000-300,0,rand.nextFloat()*1000-400), 0,0,0, 1);
			entity4.getModel().getTexture().setTransParent(true);
			entities.add(entity4);
		}
		
		List<Terrain> terrains = new ArrayList<>();
		Terrain terrain = new Terrain(new Vector3f(0, 0,0), loader, pack, blendMap,"/res/heightmap.png");
		terrains.add(terrain);
		terrains.add(new Terrain(new Vector3f(1, 0,0), loader, pack, blendMap,"/res/heightmap.png"));
		//***********************************************//
		TexturedModel person = null;
		try {
			person = new TexturedModel(objLoader.loadFile("/res/person.obj"), new Texture("/res/playerTexture.png"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		Player player = new Player(person, new Vector3f(300,0,-380f), 0, 0, 0, 0.5f);
		
		entities.add(player);
		//***********************************************//
		TexturedModel lamp = new TexturedModel(objLoader.loadFile("/res/lamp.obj"), new Texture("/res/lamp.png"));
		
		entities.add(new Entity(lamp, new Vector3f(185,0f,-293), 0, 0, 0, 1)) ;
		entities.add(new Entity(lamp, new Vector3f(370,0f,-300), 0, 0, 0, 1)) ;
		entities.add(new Entity(lamp, new Vector3f(293,0f,-305), 0, 0, 0, 1)) ;
		
		//***********************************************//
		List<Light>lights = new ArrayList<>();
		lights.add(new Light(new Vector3f(0,10000,-7000),new Vector3f(0.4f,0.4f,0.4f)));
		lights.add(new Light(new Vector3f(185,10,-293),new Vector3f(2,0,0),new Vector3f(1.0f,0.01f,0.002f)));
		lights.add(new Light(new Vector3f(370,17,-300),new Vector3f(0,2,2),new Vector3f(1.0f,0.01f,0.002f)));
		lights.add(new Light(new Vector3f(293,7,-305),new Vector3f(2,2,0),new Vector3f(1.0f,0.01f,0.002f)));
		
		
		
		//***********************************************//
		Camera camera = new Camera(player,window);
		//***********************************************//
		while(!window.windowShouldClose()){
			
			if ( window.isResized() ) {
	            GL11.glViewport(0, 0, window.getWidth(), window.getHeight());
	            window.setResized(false);
	        }
			
			for (Terrain terrain2 : terrains) {
				msrender.processTerrain(terrain2);
			}
			for (Entity entity2 : entities) {
				msrender.processEntity(entity2);
			}
			camera.move();
			player.move(terrain);
			
			msrender.render(lights, camera);
			window.update();
		}
		msrender.cleanUp();
		loader.cleanUp();
	}

}
