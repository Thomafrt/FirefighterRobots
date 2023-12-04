import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Robot {

	public int energy;
	public int water;
	public Grid knownGrid;
	public Grid realGrid;
	public boolean atBase;
	public boolean inActivity;
	public Cell currentCell;
	public List<Coordonnee> path;
	public Base base;
	
	public Robot(Grid knownGrid, Grid realGrid, Base base) {
		this.energy=30;
		this.water=20;
		this.knownGrid=knownGrid;
		this.realGrid=realGrid;
		this.atBase=true;
		this.inActivity=false;
		this.currentCell=knownGrid.getCell(base.coord);
		this.path=new ArrayList<Coordonnee>();
		this.base=base;

	}

	public void turn(){
		for (int i=0; i<knownGrid.getSize(); i++) {
			for (int j=0; j<knownGrid.getSize(); j++) {
			}
		}
		// System.out.println("Robot, coord: "+this.currentCell.coordonnee.x+" "+this.currentCell.coordonnee.y+", pathsize: "+path.size());
		// si je suis à la base et que je suis plein, je mets à jour la carte, je trace un itinéraire et je pars...
		if(atBase){
			if(energy>=30){
				this.energy=30;
				this.knownGrid=base.knownGrid.clone();
				this.path=getnewPath(currentCell.coordonnee);
				if(this.path.size()>0){
				this.inActivity=true;
				this.atBase=false;
				moveOnPath();
				}
				//... sinon je me recharge de 0.1 énérgie
			}else{
				energy+=10;
			}
		}
		else{
			// si je suis sur le retour, j'avance.
			if(!inActivity){
				moveOnPath();
			}
			else{
				// si la case est en feu et que j'ai de l'eau je lache de l'eau.
				//inActivity
				if(currentCell.state==2){
					if(water>0){
						water-=1;
						this.currentCell.fire-=1;
						this.realGrid.getCell(currentCell.coordonnee).fire-=1;
						setProximity();
						notifications();
						if(this.currentCell.fire==0){
							this.currentCell.state=3;
							this.realGrid.getCell(currentCell.coordonnee).state=3;
							notifications();
							this.path=getnewPath(currentCell.coordonnee);
						}
						System.out.println("fire on" +currentCell.coordonnee.x +" "+currentCell.coordonnee.y+", "+realGrid.getCell(currentCell.coordonnee).fire+" left, state: "+currentCell.state);

					}
				}
				else{
					// sinon j'avance
					moveOnPath();
					// si je n’ai plus d’énérgie, je planifie un trajet de retour et je me rend indisponible	
					if(energy<=getDistanceToBase(currentCell.coordonnee)){
					this.inActivity=false;
					this.path=getPathHome();
					}
				}
			}
			energy-=1;
		}
		knownGrid.incrementDuration();
	}

	public void setProximity(){
		this.knownGrid.addfireProximity(currentCell,1,new ArrayList<Cell>());	
		base.knownGrid.addfireProximity(currentCell,1,new ArrayList<Cell>());
	}


		public void notifications(){
			this.currentCell.duration=0;
			if(this.currentCell.hasHuman==1){
				if(this.currentCell.state==3){
					this.currentCell.hasHuman=3;
				}
				else this.currentCell.hasHuman=2;
			}
			Cell cell=this.currentCell;
			// update knownGrid
			this.knownGrid.getCell(currentCell.coordonnee)
			.set(cell.state, cell.hasHuman, cell.fire, 0);
			// update base.knownGrid
			this.base.knownGrid.getCell(currentCell.coordonnee)
			.set(cell.state, cell.hasHuman, cell.fire, 0);
		}

		public void moveOnPath(){
			if (this.path.size()==0) this.path=getnewPath(currentCell.coordonnee);
			this.currentCell=realGrid.getCell(this.path.get(0));
			this.path.remove(0);
			if(currentCell.state==0){
					this.atBase=true;
					this.water=20;
				}
				else{
				// s'il y a quelque chose à signaler sur cette case je le signale
				notifications();
				}
		}

		public List<Coordonnee> getnewPath(Coordonnee from){
			int distanceToBase = getDistanceToBase(from);
			// le détour qu'on peut se permettre pour tout de meme rentrer.
			int overflow = ((int)(energy-1)-distanceToBase)/2;

			// les cases que l'on peut explorer avant de devoir rentrer
			Set<Cell> possibleCells = getPossibleSpace(from, overflow);
			Cell objective = null;

			// Si il y a un feu et qu'il est encore temps de l'eteindre on va eteindre le feu

			//trouve le feu le plus proche
			int minDistance = 1000;

				for (Cell cell : possibleCells) {
				if (cell.state == 2) {
					int distance = Math.abs(cell.coordonnee.x-currentCell.coordonnee.x)+Math.abs(cell.coordonnee.y-currentCell.coordonnee.y);
					if (distance < minDistance) {
						minDistance = distance;
						objective = cell;
					}
				}
			}

			if (objective != null) {
				return getPath(from, objective.coordonnee);
			} else {
			// sinon on va voir une case safe connu depuis longtemps en rapport "age de l'info"²/distance
			int maxScore = 0;

				for (Cell cell : possibleCells) {
				if (cell.state == 1) {
					int distance = Math.abs(cell.coordonnee.x-currentCell.coordonnee.x)+Math.abs(cell.coordonnee.y-currentCell.coordonnee.y);
					int score = (int)((Math.pow(cell.duration, 2) / distance)*cell.fireProximity);
					if (score > maxScore) {
						maxScore = score;
						objective = cell;
					}
				}	
			}
			System.out.println("maxScore: "+maxScore);
			if(objective!=null){
				return getPath(from, objective.coordonnee);
			}
			// si il n'y a pas de case en feu, ni inconnu ni safe, c'est que tout est déja brulé, on rentre
			return getPathHome();	
			}
		}

		public int getDistanceToBase(Coordonnee from){
			return Math.abs(from.x-base.coord.x)+Math.abs(from.y-base.coord.y);
		}

		// renvoie la liste des cases que le robot peut encore atteindre vant de devoir rentrer
		public Set<Cell> getPossibleSpace(Coordonnee from, int overflow){
			Set<Cell> possibleCells = new HashSet<>();
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
					if((!(i==from.x && j==from.y))
					&& (!(i==base.coord.x && j==base.coord.y))
					&& i>=0 && i<knownGrid.size && j>=0 && j<knownGrid.size) 
					possibleCells.add(knownGrid.getCell(new Coordonnee(i, j)));
					}
				}
			}
			return possibleCells;
		}
	
		public List<Coordonnee> getPathHome(){		
			return getPath(this.currentCell.coordonnee, base.coord);
		}
	
		public List<Coordonnee> getPath(Coordonnee C1, Coordonnee C2){
			List<Coordonnee> path= new ArrayList<Coordonnee>();
			int x=C1.x;
			int y=C1.y;
			int deltaX=C2.x-C1.x;
			int deltaY=C2.y-C1.y;
			while(deltaX!=0 || deltaY!=0){
				if(deltaX!=0 && deltaY!=0){
					if((int)Math.random()==1){
						if(deltaX>0){
							deltaX-=1;
							x++;
							
						}
						else if(deltaX<0){
							deltaX+=1;
							x--;
							
						}
					}
					else if(deltaY!=0){
						if(deltaY>0){
							deltaY-=1;
							y++;
													
						}
						else if(deltaY<0){
							deltaY+=1;
							y--;
													
						}
					}
				}
				else if(deltaX!=0){
				 	if(deltaX>0){
							deltaX-=1;
							x++;
							
						}
						else if(deltaX<0){
							deltaX+=1;
							x--;
							
						}
				}
				else if(deltaY!=0){
					if(deltaY>0){
							deltaY-=1;
							y++;
													
						}
						else if(deltaY<0){
							deltaY+=1;
							y--;
													
						}
				}
				path.add(new Coordonnee(x, y));
			}
		return path;
		}

}