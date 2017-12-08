import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Account {
	private int val;
	private String oper;
	private static Object vigile = new Object();
	private static Lock lock = new ReentrantLock();

	public static void main(String[] args) {
		final Account acc = new Account();
		Thread retrait = new Thread(new Runnable() {
			@Override
			public void run() {
				for (int i = 0; i < 5000; ++i) {

					// synchronized (vigile) {
					lock.lock();
					try {
						acc.oper = "retrait"; // non ce n'est pas thread safe car le thread 1 risque de s'arrêter au
						// milieu des 3 opérations
						// et si le thread 2 prend sa place, le syso n'affichera pas 50 et retrait
						acc.val = 50;
						System.out.println(acc.oper + " " + acc.val + "euros");
					} finally {
						lock.unlock();

					}
				}
			}
		});
		Thread depot = new Thread(new Runnable() {
			@Override
			public void run() {
				for (int i = 0; i < 5000; ++i) {
					lock.lock();
					try {
						acc.oper = "depot";
						acc.val = 100;
						System.out.println(acc.oper + " " + acc.val + "euros");

					} finally {
						lock.unlock();
					}
				}
			}
		});
		retrait.start();
		depot.start();

		// Exo 1 Question 3 réponse : this est accessible de l'exterieur et on ne veut
		// pas de jeton accessible de l'exterieur, on cherche toujours avec la
		// visibilité la plus faible
		// On evite ainsi les deadlocks
	}
}