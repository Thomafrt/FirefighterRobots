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
	public Coordonnee[] path;
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
				this.path=base.getGoing();
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
						base.getnewPath(currentCase.coordonnee, this.energy*10);
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
					if(energy==0){
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
			this.path=base.getPathHome(this.currentCase.coordonnee);
		}

}