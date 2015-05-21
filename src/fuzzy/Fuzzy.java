package fuzzy;

public class Fuzzy {
	
	private static double temperatura; //Entrada
	private static double volume; //Entrada
	private static double pressao; //Saída desfuzzificada
	
	//Conjuntos fuzzy de entrada. As variáveis irão armanezar as pertinências de cada conjunto
	private static double temperaturaBaixa;
	private static double temperaturaMedia;
	private static double temperaturaAlta;
	private static double volumePequeno;
	private static double volumeMedio;
	private static double volumeGrande;
	
	//Conjuntos fuzzy de saída. As variáveis irão armanezar as pertinências de cada conjunto
	private double pressaoBaixa;
	private double pressaoMedia;
	private double pressaoAlta;
	
	/*Regras de Inferência (9 regras)
	 * 0 - baixo/pequeno | 1 - medio | 2 - alta/grande
	 */
    private static int[][] regrasInferencia = new int[][]{
    		{0,0,0}, //Se (Temperatura é Baixa) e (Volume é Pequeno) Então (Pressão é Baixa)
            {1,0,0}, //Se (Temperatura é Média) e (Volume é Pequeno) Então (Pressão é Baixa)
            {2,0,1}, //Se (Temperatura é Alta) e (Volume é Pequeno) Então (Pressão é Média)
            {0,1,0}, //Se (Temperatura é Baixa) e (Volume é Médio) Então (Pressão é Baixa)
            {1,1,1}, //Se (Temperatura é Média) e (Volume é Médio) Então (Pressão é Média)
            {2,1,2}, //Se (Temperatura é Alta) e (Volume é Médio) Então (Pressão é Alta)
            {0,2,1}, //Se (Temperatura é Baixa) e (Volume é Grande) Então (Pressão é Média)
            {1,2,2}, //Se (Temperatura é Média) e (Volume é Grande) Então (Pressão é Alta)
            {2,2,2}, //Se (Temperatura é Alta) e (Volume é Grande) Então (Pressão é Alta)

          };

	public static void main(String[] args) {
		
		//Variáveis de entrada
		temperatura = 965.0;
		volume = 11.0;
		System.out.println("TEMPERATURA: " + temperatura);
		System.out.println("VOLUME: " + volume);
		
		/* FUZZIFICAÇÃO
		 * Obtem o grau de pertinência que cada entrada pertence a cada conjunto fuzzy de entrada */
		System.out.println("Iniciando Fuzzificação...");
		fuzzificacao();
		
		/* MOTOR DE INFERÊNCIA 
		 * Avaliação de regras + Agregração de Regras (Método de Mandami)
		 * Avaliação de regras -> Os antecedentes de cada regra são processados afim de se obter 
		 * os conjuntos de saída (consequentes) */
	
		avaliaRegras();
		

	}
	
	private static void fuzzificacao(){
		
		//Fuzzificação da temperatura
		temperaturaBaixa = pertinenciaTrapezoidal(temperatura, 800, 1000, 800, 900);
		System.out.println("Grau de pertença da temperatura ao conjunto 'temperaturaBaixa': " + temperaturaBaixa);
		temperaturaMedia = pertinenciaTriangular(temperatura, 900, 1100, 1000);
		System.out.println("Grau de pertença da temperatura ao conjunto 'temperaturaMedia': " + temperaturaMedia);
		temperaturaAlta = pertinenciaTrapezoidal(temperatura, 1000, 1200, 1100, 1200);
		System.out.println("Grau de pertença da temperatura ao conjunto 'temperaturaAlta': " + temperaturaAlta);
		
		//Fuzzificação do volume
		volumePequeno = pertinenciaTrapezoidal(volume, 2.0, 7.0, 2.0, 4.5);
		System.out.println("Grau de pertença do volume ao conjunto 'volumePequeno': " + volumePequeno);
		volumeMedio = pertinenciaTriangular(volume, 4.5, 9.5, 7.0);
		System.out.println("Grau de pertença do volume ao conjunto 'volumeMedio': " + volumeMedio);
		volumeGrande = pertinenciaTrapezoidal(volume, 7.0, 12.0, 9.5, 12.0);
		System.out.println("Grau de pertença do volume ao conjunto 'volumeGrande': " + volumeGrande);
		
	}
	
	private static double pertinenciaTriangular(double x, double limInferior, double limSuperior, double m){
		
		double pertinencia = 0;
		
		if(x <= limInferior){
			pertinencia = 0;
		}
		else if(x > limInferior && x < m){
			pertinencia = (x - limInferior)/(m - limInferior);
		}
		else if(x > m && x < limSuperior){
			pertinencia = (limSuperior - x)/(limSuperior - m);
		}
		else if(x > limSuperior){
			pertinencia = 0;
		}
		
		return pertinencia;
		 
	}
	
	private static double pertinenciaTrapezoidal(double x, double limInferior, double limSuperior, double m, double n){
		
		 double pertinencia = 0;
		 
		 if(x < limInferior){
			 pertinencia = 0;
		 }
		 else if(x >= limInferior && x <= m){
			 pertinencia = (x - limInferior)/(m - limInferior);
		 }
		 else if(x >= m && x <= n){
			 pertinencia = 1.0;
		 }
		 else if(x >= n && x <= limSuperior){
			 pertinencia = (limSuperior - x)/(limSuperior - n);
		 }
		 else if(x > limSuperior){
			 pertinencia = 0;
		 }
		 
		 return pertinencia;
		 
	}
	
	private void avaliaRegras(){
		
	}
	

}
