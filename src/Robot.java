import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Robot {

	public int energy;
	public int water;
	public int wateringTimer;
	public double speed=0.1;
	public Grid knownGrid;
	public boolean atBase;
	public boolean inActivity;
	public Modification[] modifications;
	public Cell currentCase;
	public List<Coordonnee> path;
	public Base base;
	
	public Robot() {
		// TODO Auto-generated constructor stub
	}

	/*
	 * return true if the robot is at the base at the end of its move
	 */
	public void turn(){
		// si je suis à la base et que je suis plein, je trace un itinéraire et je pars, sinon je me recharge de 0.1 énérgie
		// si je suis sur le retour, j'avance, si j'arrive à la base je fais le plein d'eau.
		// si je suis sur le terrain et que je n'ai plus d'énergie, je rentre à la base
		// si je suis sur le terrain et qu'il y a un feu à éteindre, je l'éteint, sinon je me déplace
		// si j'ai éteint un feu, je modifie mon path pour aller à la case la plus proche d'un feu à éteindre
		// après mon déplacement si il y a un humain je le signale, si la case sur laquelle je suis à un état différent de celle que je connais, je le signale
		if(atBase){
			if(energy==3){
				this.path=getGoing();
				moveOnPath();
			}else{
				energy+=0.1;
			}
		}else{
			decisionOnField(knownGrid);
		}
	}


		public void decisionOnField(Grid knownGrid){
			if(!inActivity){
			moveOnPath();
			if(currentCase.state==0){
				this.atBase=true;
				this.water=2;
			}
			}
			else{
				if(this.wateringTimer>0){
					this.wateringTimer-=1;
					if(this.wateringTimer==0){
						update(4);
						getnewPath(currentCase.coordonnee);
					}
				}
				if(currentCase.state==2){
					this.water-=1;
					update(3);
					this.wateringTimer=9;
					modifications[modifications.length]=new Modification(currentCase, 2);
					// TODO update knownGrid
				}
				else{
					moveOnPath();
					if(currentCase.asHuman){
						update(currentCase.state, true);
					}
					else if(currentCase.state!=knownGrid.getCell(currentCase.coordonnee).state){
						update(currentCase.state);
						// TODO update knownGrid
						// TODO update path
					}
					if(energy*10<=getDistanceToBase(currentCase.coordonnee)){
						backToBase();
					}
				}
			}
			// si je vois un feu et que j'ai de l'eau, je lache de l’eau sur le feu
			// j’explore la case la plus proche selon un rapport distance - proximité avec un feu connu - date de dernière update.
			// si je vois un humain, je le signale,
			// si je n’ai plus d’énérgie, je rentre
		}



		public void update(int state){
			// je met à jour la case sur laquelle je suis
			// je met à jour la carte globale
			// je met à jour mon path
		}

		public void update(int state, boolean human){
			// je met à jour la case sur laquelle je suis
			// je met à jour la carte globale
			// je met à jour mon path
		}

		public void moveOnPath(){
			// je me déplace sur le chemin
		}

		public void backToBase(){
			this.inActivity=false;
			this.path=getPathHome();
		}

		public List<Coordonnee> getnewPath(Coordonnee from){
			//TODO définir la cellule à allez voir en fonction de la distance et de la position du robot
			int distanceToBase = getDistanceToBase(from);
			// le détour qu'on peut se permettre pour tout de meme rentrer.
			int overflow = ((int)(energy-0.1)*10-distanceToBase)/2;

			// les cases que l'on peut explorer avant de devoir rentrer
			Set<Cell> possibleCells = getPossibleSpace(from, overflow);
			Cell objective = null;

			// Si il y a un feu et qu'il est encore temps de l'eteindre on va eteindre le feu

			//trouve le feu le plus proche
			double minDistance = Double.MAX_VALUE;

				for (Cell cell : possibleCells) {
				if (cell.state == 2) {
					double distance = Math.abs(cell.coordonnee.x-currentCase.coordonnee.x)+Math.abs(cell.coordonnee.y-currentCase.coordonnee.y);
					if (distance < minDistance) {
						minDistance = distance;
						objective = cell;
					}
				}
			}

			if (objective != null) {
				return getPath(from, objective.coordonnee);
			} else {
			// sinon on va voir une case inconnu
				for (Cell cell : possibleCells) {
					if (cell.state == 5) {
						return getPath(from, cell.coordonnee);
					}
				}
			// sinon on va voir une case safe connu depuis longtemps en rapport "age de l'info"²/distance
			double maxScore = 0;

				for (Cell cell : possibleCells) {
				if (cell.state == 1) {
					double distance = Math.abs(cell.coordonnee.x-currentCase.coordonnee.x)+Math.abs(cell.coordonnee.y-currentCase.coordonnee.y);
					double score = Math.pow(cell.duration, 2) / distance;
					if (score > maxScore) {
						maxScore = score;
						objective = cell;
					}
				}	
			}
			// si il n'y a pas de case en feu, ni inconnu ni safe, c'est que tout est déja brulé, on rentre
			return getPathHome();	
			}
		}

		public int getDistanceToBase(Coordonnee from){
			return Math.abs(from.x-base.coord.x)+Math.abs(from.y-base.coord.y);
		}

		// renvoie la liste des cases que le robot peut encore atteeindre vant de devoir rentrer
		public Set<Cell> getPossibleSpace(Coordonnee from, int overflow){
			Set<Cell> possibleCells = new HashSet<>();
			Set<Cell> rectangle = new HashSet<>();
			Coordonnee upleft;
			Coordonnee upright;
			Coordonnee downleft;
			Coordonnee downright;

			// on définit le rectangle ente la base et le robot
			if(from.x<base.coord.x && from.y<base.coord.y){
				upleft=from;
				upright=new Coordonnee(base.coord.x, from.y);
				downleft=new Coordonnee(from.x, base.coord.y);
				downright=base.coord;
			}
			else if (from.x<base.coord.x && from.y>base.coord.y){
				upleft=new Coordonnee(from.x, base.coord.y);
				upright=base.coord;
				downleft=from;
				downright=new Coordonnee(base.coord.x, from.y);
			}
			else if (from.x>base.coord.x && from.y<base.coord.y){
				upleft=new Coordonnee(base.coord.x, from.y);
				upright=from;
				downleft=base.coord;
				downright=new Coordonnee(from.x, base.coord.y);
			}
			else{
				upleft=base.coord;
				upright=new Coordonnee(from.x, base.coord.y);
				downleft=new Coordonnee(base.coord.x, from.y);
				downright=from;
			}

			// rajoute les cases dans le rectangle plus le débordement moins les cases trop loin dans les angles
			for(int i=upleft.x-overflow;i<=upright.x+overflow;i++){
				for(int j=upleft.y-overflow;j<=downleft.y+overflow;j++){
					if(!(i<upleft.x && j<upleft.y && (Math.abs(i-upleft.x)+Math.abs(j-upleft.y))>overflow
						|| i<downleft.x && j>downleft.y && (Math.abs(i-downleft.x)+Math.abs(j-downleft.y))>overflow
						|| i>upright.x && j<upright.y && (Math.abs(i-upright.x)+Math.abs(j-upright.y))>overflow
						|| i>downright.x && j>downright.y && (Math.abs(i-downright.x)+Math.abs(j-downright.y))>overflow
					)){
					possibleCells.add(knownGrid.getCell(new Coordonnee(i, j)));
					}
				}
			}
			return possibleCells;
		}

		public List<Coordonnee> getGoing(){
			return getnewPath(base.coord);
		}
	
		public List<Coordonnee> getPathHome(){		
			return getPath(this.currentCase.coordonnee, base.coord);
		}
	
		public List<Coordonnee> getPath(Coordonnee C1, Coordonnee C2){
			List<Coordonnee> path= new ArrayList<Coordonnee>();
			int deltaX=C2.x-C1.x;
			int deltaY=C2.y-C1.y;
			while(deltaX!=0 || deltaY!=0){
				if(deltaX!=0 && deltaY!=0){
					if((int)Math.random()==1){
						if(deltaX>0){
							deltaX-=1;
							path.add(new Coordonnee(C1.x-1, C1.y));
						}
						else if(deltaX<0){
							deltaX+=1;
							path.add(new Coordonnee(C1.x+1, C1.y));
						}
					}
					else{
						if(deltaY>0){
							deltaY-=1;
							path.add(new Coordonnee(C1.x, C1.y-1));						
						}
						else if(deltaY<0){
							deltaY+=1;
							path.add(new Coordonnee(C1.x, C1.y+1));						
						}
					}
				}
				else if(deltaX!=0){
				 	if(deltaX>0){
							deltaX-=1;
							path.add(new Coordonnee(C1.x-1, C1.y));
						}
						else if(deltaX<0){
							deltaX+=1;
							path.add(new Coordonnee(C1.x+1, C1.y));
						}
				}
				else if(deltaY!=0){
					if(deltaY>0){
							deltaY-=1;
							path.add(new Coordonnee(C1.x, C1.y-1));						
						}
						else if(deltaY<0){
							deltaY+=1;
							path.add(new Coordonnee(C1.x, C1.y+1));						
						}
				}
			}
		return path;
		}

}