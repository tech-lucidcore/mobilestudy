package kr.testuser.realtest;

import java.io.InputStream;
import java.io.OutputStream;
import java.lang.ref.WeakReference;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class RealTester {
	List<RTSocketHolder> __holderList = new ArrayList<>();
	RTAcceptor __acceptor;
	
	public void start() {
		__acceptor = new RTAcceptor();
		__acceptor.start();
	}
	
	public void stop() {
		if (__acceptor != null) {
			__acceptor.interrupt();
		}
	}
	
	public void sendData(byte[] data) {
		for (RTSocketHolder holder : __holderList) {
			holder.sendData(data);
		}
	}
	
	public class RTAcceptor extends Thread {
		public void run() {
			try {
				ServerSocket listener = new ServerSocket(5555);
				while (true) {
					if (isInterrupted() == true) {
						break;
					}
					Socket socket = listener.accept();
					RTSocketHolder holder = new RTSocketHolder(socket);
					holder.start();
System.out.println("RTAcceptor accepted!!!");
					__holderList.add(holder);
				}
				listener.close();
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
			}
		}
	}
	
	class RTSocketHolder {
		Socket __socket;
		InputStream __is;
		OutputStream __os;
		ReadThread __readThread;
		WriteThread __writeThread;
		public RTSocketHolder(Socket socket) {
			__socket = socket;
		}
		
		public void start() {
			try {
				__readThread = new ReadThread(this, __socket.getInputStream());
				__readThread.start();
				__writeThread = new WriteThread(this, __socket.getOutputStream());
				__writeThread.start();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		public void stop() {
			if (__readThread != null) {
				__readThread.interrupt();
			}
			if (__writeThread != null) {
				__writeThread.interrupt();
			}
			__holderList.remove(this);
		}
		
		public void sendData(byte[] data) {
			__writeThread.pushData(data);
		}
	}
	
	class ReadThread extends Thread {
		WeakReference<RTSocketHolder> __holder;
		private InputStream __is;

		public ReadThread(RTSocketHolder holder, InputStream is) {
			__holder = new WeakReference<RTSocketHolder>(holder);
			__is = is;
		}
		
		public void run() {
			byte[] readData = new byte[8192];
			try {
				while (true) {
					int readLen = __is.read(readData);
					if (isInterrupted() == true) {
						break;
					}
					if (readLen <- 0) {
						break;
						// 현재는 처리하지 않ㅇ므로 나중에 처리하자.
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			if (__holder.get() != null) {
				__holder.get().stop();
			}
		}
	}
	
	class WriteThread extends Thread {
		WeakReference<RTSocketHolder> __holder;
		private OutputStream __os;
		private BlockingQueue<byte[]> dataQueue;

		public WriteThread(RTSocketHolder holder, OutputStream os) {
			__holder = new WeakReference<RTSocketHolder>(holder);
			__os = os;
			dataQueue = new ArrayBlockingQueue<>(10000);
		}
		
		public void pushData(byte[] aData) {
			try {
				if (dataQueue.remainingCapacity() == 0) {
					return;
				}
				dataQueue.add(aData);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		public void run() {
			try {
				while (true) {
					if (isInterrupted() == true) {
						break;
					}
					byte[] sendData = dataQueue.take();
					__os.write(sendData);
					__os.flush();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			if (__holder.get() != null) {
				__holder.get().stop();
			}
		}
	}
}