package fuzzy;

public class Fuzzy {
	
	private static final int QUANT_INTERVALOS = 100; //Quantidade de intervalos em cada conjunto
	
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
	
	//Limites em relação ao eixo X para a definição dos conjuntos fuzzy da pressão
	private static final int PRESSAO_BAIXA_INFERIOR = 0;
	private static final int PRESSAO_BAIXA_SUPERIOR = 8;
	private static final int PRESSAO_MEDIA_INFERIOR = 6;
	private static final int PRESSAO_MEDIA_SUPERIOR = 10;
	private static final int PRESSAO_ALTA_INFERIOR = 8;
	private static final int PRESSAO_ALTA_SUPERIOR = 12;
	
	/*Regras de Inferência (9 regras)
	 * {temperatura, volume, pressao}
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
		
		System.out.println("--------------------------------");
		System.out.println("Iniciando Avaliação de Regras...");
		//9 regras, cada coluna armazena: [0] - pertinencia, [1] - limite inferior, [2] - limite superior
		double regrasAvaliadas[][] = avaliaRegras();
		
		/*for(int i = 0; i < regrasAvaliadas.length; i++){
			System.out.println("Limite Inferior: " + regrasAvaliadas[i][1] + " | " + "Limite Superior: " + regrasAvaliadas[i][2]);
		}*/
		
		System.out.println("--------------------------------");
		System.out.println("Iniciando Agregação de Regras...");
		double[] pertinenciasAgregadas = agregaRegras(regrasAvaliadas);
		
		System.out.println("--------------------------------");
		System.out.println("Desfuzzificação...");
		pressao = desfuzzificacao(pertinenciasAgregadas);
		
		System.out.println("\nRESULTADO: Pressão = " + pressao);
		

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
	
	private static double[][] avaliaRegras(){
		
		double regrasAvaliadas[][] = new double[9][3]; //9 regras, cada coluna armazena: [0] - pertinencia, [1] - limite inferior, [2] - limite superior
		for(int i = 0; i < regrasInferencia.length; i++){
			System.out.println("\n=> Regra " + i);
			//Armazena as pertinências (eixo Y)
			double pertTemp = 0;
			double pertVol = 0;
			double pertPress = 0;
			
			//Armazena os limites do eixo X da região da pressão
			int pressaoInferior = 0;
			int pressaoSuperior = 0;
			
			int temperatura = regrasInferencia[i][0];
			int volume = regrasInferencia[i][1];
			int pressao = regrasInferencia[i][2];
			
			switch(temperatura){
				case 0:
					pertTemp = temperaturaBaixa;
					System.out.println("Temperatura Baixa: " + pertTemp);
					break;
				case 1:
					pertTemp = temperaturaMedia;
					System.out.println("Temperatura Média: " + pertTemp);
					break;
				case 2:
					pertTemp = temperaturaAlta;
					System.out.println("Temperatura Alta: " + pertTemp);
					break;
			}
			
			switch(volume){
				case 0:
					pertVol = volumePequeno;
					System.out.println("Volume Pequeno: " + pertVol);
					break;
				case 1:
					pertVol = volumeMedio;
					System.out.println("Volume Médio: " + pertVol);
					break;
				case 2:
					pertVol = volumeGrande;
					System.out.println("Volume Grande: " + pertVol);
					break;
			}
			
			//Como os antecedentes de todas as regras são compostos pelo operador E, escolhe-se a menor pertinência
			if(pertTemp < pertVol){
				pertPress = pertTemp;
			}
			else{
				pertPress = pertVol;
			}
			
			switch(pressao){
				case 0: 
					pressaoInferior = PRESSAO_BAIXA_INFERIOR;
					pressaoSuperior = PRESSAO_BAIXA_SUPERIOR;
					System.out.println("Pressão Baixa: " + pertPress);
					break;
				case 1:
					pressaoInferior = PRESSAO_MEDIA_INFERIOR;
					pressaoSuperior = PRESSAO_MEDIA_SUPERIOR;
					System.out.println("Pressão Média: " + pertPress);
					break;
				case 2:
					pressaoInferior = PRESSAO_ALTA_INFERIOR;
					pressaoSuperior = PRESSAO_ALTA_SUPERIOR;
					System.out.println("Pressão Alta: " + pertPress);
					break;
			}
			
			regrasAvaliadas[i][0] = pertPress;
			regrasAvaliadas[i][1] = pressaoInferior;
			regrasAvaliadas[i][2] = pressaoSuperior;
			
		}
		
		return regrasAvaliadas;
		
	}
	
	//Cria um vetor contendo os valores resultantes da agregação das regras
	private static double[] agregaRegras(double regrasAvaliadas[][]){
		
		double eixoY[] = new double[QUANT_INTERVALOS]; //cada posição do vetor irá armazenar uma pertinência. A quantidade de pertinências armazenadas é a quantidade de intervalos
		double eixoX[] = new double[QUANT_INTERVALOS]; //cada posição irá armazenar um valor do eixo X (pressão);
		double fatorIncremento = (double)(PRESSAO_ALTA_SUPERIOR - PRESSAO_BAIXA_INFERIOR)/QUANT_INTERVALOS;
		double soma = 0;
		
		System.out.println("Fator de incremento: " + fatorIncremento);
		
		System.out.println("Pontos do gráfico: ");
		System.out.println("x => y");
		
		//Inicializa o vetor do eixoY com zero
		for(int i = 0; i < eixoY.length; i++){
			eixoY[i] = 0;
		}
		
		for(int i = 0; i < QUANT_INTERVALOS; i++){
			 eixoX[i] = soma;
			 
			 for(int j = 0; j < regrasAvaliadas.length; j++){
				 
				 double limInferior = regrasAvaliadas[j][1];
				 double limSuperior = regrasAvaliadas[j][2];
				
				 
				 //System.out.println("eixo y: " + eixoY[i] + " | " + " regra: " + regrasAvaliadas[j][0] + " | limInferior: " + limInferior + " | " + "limSuperior: " + limSuperior + " | Soma: " + soma);
				 if(soma >= limInferior && soma <= limSuperior){ //x entra dentro da faixa dessa regra
					 double limite = regrasAvaliadas[j][0];
					 double y = limite;
					 if(soma >= 4 && soma <= 8){ //pressao baixa - trapezoidal
						 double p = pertinenciaTrapezoidal(soma, limInferior, limSuperior, 4, 5);
						 if(p < limite){
							 y = p;
						 }
					 }
					 if(soma >= 6 && soma <= 10){ //pressao media - triangular
						 double p = pertinenciaTriangular(soma, limInferior, limSuperior, 8);
						 if(p < limite){
							 y = p;
						 }
					 }
					 if(soma >= 8 && soma <= 12){ //pressao alta - trapezoidal
						 double p = pertinenciaTrapezoidal(soma, limInferior, limSuperior, 11, 12);
						 if(p < limite){
							 y = p;
						 }
					 }
					 
					 if(y > eixoY[i]){
						 eixoY[i] = y;
					 }
				 }
			 }
			 
			 soma += fatorIncremento;	
		}
		
		for(int i = 0; i < QUANT_INTERVALOS; i++){
			System.out.println(eixoX[i] + " => " + eixoY[i]);
		}
		
		return eixoY;
		
	}
	
	private static double desfuzzificacao(double[] eixoY){
		
		double[] eixoX = new double[QUANT_INTERVALOS];
		double fatorIncremento = (double)(PRESSAO_ALTA_SUPERIOR - PRESSAO_BAIXA_INFERIOR)/QUANT_INTERVALOS;
		double soma = 0;
        double numerador = 0;
        double denominador = 0;
        
        for(int i = 0; i < QUANT_INTERVALOS; i++){
			 eixoX[i] = soma;
			 soma += fatorIncremento;
        }
        
        for (int i = 0; i < QUANT_INTERVALOS; i++) {
            numerador += eixoX[i] * eixoY[i];
            denominador += eixoY[i];
        }
        
        return numerador/denominador;
        
	}

}
