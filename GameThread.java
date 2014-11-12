package uk.ac.reading.sis05kol.mooc;

//Other parts of the android libraries that we use
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
//import android.view.MotionEvent;

public class TheGame extends GameThread{

	//Will store the images
	private Bitmap mBall;
	private Bitmap mPaddle;
	private Bitmap mBumper;
	
	//The X and Y position of the paddle on the screen (middle of ball)
	private float mPaddleX = 0;
	private float mPaddleY = 0;
	
	//Collision Distances
	private float mMinDistBallPaddle = 0;
	private float mMinDistBallBumper = 0;
	
	//The X and Y position of the ball on the screen (middle of ball)
	private float mBallX = 0;
	private float mBallY = 0;
	
	//X & Y position of bumper
	private float mBumperX = 0;
	private float mBumperY = 0;
	
	//The speed (pixel/second) of the ball in direction X and Y
	private float mBallSpeedX = 0;
	private float mBallSpeedY = 0;
	private float mBallSpeedInitX = 0;
	private float mBallSpeedInitY = 0;
	private float mInitVelocity = 0;
	private float newVelocity = 0;
	private float nextVelocity = 0;
	private float mBumperSpeedX = 0;
	private float mBumperSpeedY = 0;

	//This is run before anything else, so we can prepare things here
	public TheGame(GameView gameView) {
		//House keeping
		super(gameView);
		
		//Prepare the image so we can draw it on the screen (using a canvas)
		mBall = BitmapFactory.decodeResource
				(gameView.getContext().getResources(), 
				R.drawable.small_red_ball);
		mPaddle = BitmapFactory.decodeResource
				(gameView.getContext().getResources(),
					R.drawable.yellow_ball);
		mBumper = BitmapFactory.decodeResource
				(gameView.getContext().getResources(),
					R.drawable.smiley_ball);
	}
	
	//This is run before a new game (also after an old game)
	@Override
	public void setupBeginning() {
		
		//Initialise speeds
		mBallSpeedX = 0; 
		mBallSpeedY = 100;
		mBallSpeedInitX = mBallSpeedX;
		mBallSpeedInitY = mBallSpeedY;
		mInitVelocity = (float) Math.sqrt(mBallSpeedInitX * mBallSpeedInitX + mBallSpeedInitY * mBallSpeedInitY);
		mBumperSpeedX = 20;
		mBumperSpeedY = 100;
		
		//Place the ball in the middle of the screen.
		//mBall.Width() and mBall.getHeigh() gives us the height and width of the image of the ball
		mBallX = mCanvasWidth / 2;
		mBallY = mCanvasHeight / 2;
		
		//Place Paddle in the centre of the screen at the bottom
		mPaddleX = mCanvasWidth / 2;
		mPaddleY = mCanvasHeight;
		
		//Place Bumper at top of screen
		mBumperX = mCanvasWidth / 2;
		mBumperY = mCanvasHeight / 10;
		
		//Calculate the distance between the centre of the Paddle and the Ball
		mMinDistBallPaddle = (mBall.getWidth() / 2) + (mPaddle.getWidth() / 2);
		mMinDistBallBumper = (mBall.getWidth() / 2) + (mBumper.getWidth() / 2);
		
	}

	@Override
	protected void doDraw(Canvas canvas) {
		//If there isn't a canvas to draw on do nothing
		//It is ok not understanding what is happening here
		if(canvas == null) return;
		
		super.doDraw(canvas);
		
		//draw the image of the ball using the X and Y of the ball
		//drawBitmap uses top left corner as reference, we use middle of picture
		//null means that we will use the image without any extra features (called Paint)
		canvas.drawBitmap(mBall, mBallX - mBall.getWidth() / 2, mBallY - mBall.getHeight() / 2, null);
		canvas.drawBitmap(mPaddle, mPaddleX - mPaddle.getWidth() / 2, mPaddleY - mPaddle.getHeight() / 2, null);
		canvas.drawBitmap(mBumper, mBumperX - mBumper.getWidth() / 2, mBumperY - mBumper.getHeight() / 2, null);
	}
	
	//This is run whenever the phone is touched by the user
	@Override
	protected void actionOnTouch(float x, float y) {
		mPaddleX = x;
	}
	
	
	//This is run whenever the phone moves around its axises 
	@Override
	protected void actionWhenPhoneMoved(float xDirection, float yDirection, float zDirection) {
	}
	
	

	
	//This is run just before the game "scenario" is printed on the screen
	@Override
	
	
	protected void updateGame(float secondsElapsed) {
		//Move the ball's X and Y using the speed (pixel/sec)
		mBallX = mBallX + secondsElapsed * mBallSpeedX;
		mBallY = mBallY + secondsElapsed * mBallSpeedY;
		
		//Moves the Bumper
		mBumperX = mBumperX + secondsElapsed * mBumperSpeedX;
		mBumperY = mBumperY + secondsElapsed * mBumperSpeedY;
		
		// Variable indicating the edges
		float mEdgeBall = (mBall.getWidth() / 2);
		float mEdgePaddle = (mPaddle.getWidth() / 2);
		float mEdgeBumper = (mBumper.getWidth() / 2);
		
		//Makes the ball rebound off the edge of the screen
		if((mBallX - mEdgeBall) <= 0 && mBallSpeedX < 0 || (mBallX + mEdgeBall) >= mCanvasWidth && mBallSpeedX > 0)
			mBallSpeedX = -mBallSpeedX;
		
		if((mBallY - mEdgeBall) <= 0 && mBallSpeedY < 0 /*|| (mBallY + mEdgeBall) >= mCanvasHeight && mBallSpeedY > 0*/)
			mBallSpeedY = -mBallSpeedY;
		
		//Makes Bumper rebound off the edges
		if((mBumperX - mEdgeBumper) <= 0 && mBumperSpeedX < 0 || (mBumperX + mEdgeBumper) >= mCanvasWidth && mBumperSpeedX > 0)
			mBumperSpeedX = -mBumperSpeedX;
		
		if((mBumperY - mEdgeBumper) <= 0 && mBumperSpeedY < 0 || (mBumperY + mEdgeBumper) >= mCanvasHeight && mBumperSpeedY > 0)
			mBumperSpeedY = -mBumperSpeedY;
		
		//Ball Bounces off Bumper
		float mBallBumperDist = 0;
		mBallBumperDist = ((mBumperX - mBallX) * (mBumperX - mBallX)) + ((mBumperY - mBallY) * (mBumperY - mBallY));
		if(mBallBumperDist <= (mMinDistBallBumper * mMinDistBallBumper))
		{
			float velocityOfBall = (float) Math.sqrt(mBallSpeedX * mBallSpeedX + mBallSpeedY * mBallSpeedY);
			
			mBallSpeedX = (mBallX - mBumperX) * 2;
			mBallSpeedY = (mBallY - mBumperY) * 2;
			
			float newVelocity = (float) Math.sqrt(mBallSpeedX * mBallSpeedX + mBallSpeedY * mBallSpeedY);
			
			mBallSpeedX = mBallSpeedX * velocityOfBall / newVelocity;
			mBallSpeedY = mBallSpeedY * velocityOfBall / newVelocity;
			
			updateScore(1);
		}
		
		
		//Ball goes out of Bounds
		if((mBallY - mEdgeBall) >= mCanvasHeight && mBallSpeedY > 0)
		{
			setState(GameThread.STATE_LOSE);
		}
		
		//Collision with Paddle	sqrt((x2 - x2)^2 + (y2 - y1)^2)
		//if(Math.sqrt(Math.pow(((mBallX + mEdgeBall) - mPaddleX), 2) + Math.pow(((mBallY + mEdgeBall) - mPaddleY), 2)) < mEdgePaddle)
		/*if(Math.sqrt(Math.pow(mBallX - mPaddleX, 2) + Math.pow(mBallY - mPaddleY, 2)) < mEdgePaddle)
		{
			mAngPaddle = (float)(1 / (Math.cos(mBallX - mPaddleX) / mEdgePaddle));
			mBallSpeedX = (float) (mBallSpeedX * Math.cos(2 * mAngPaddle) + mBallSpeedY * Math.sin(2 * mAngPaddle));
			mBallSpeedY = (float) (mBallSpeedX * Math.sin(2 * mAngPaddle) - mBallSpeedY * Math.cos(2 * mAngPaddle));
		}*/
		
	
		if(mBallSpeedY > 0)
		{
			float distBallPaddle = 0;
			//distBallPaddle = (float) (Math.pow(mPaddleX - mBallX, 2) + Math.pow(mCanvasHeight - mBallY, 2));
			distBallPaddle = ((mPaddleX - mBallX) * (mPaddleX - mBallX)) + ((mCanvasHeight - mBallY) * (mCanvasHeight - mBallY));
			if(distBallPaddle <= (mMinDistBallPaddle * mMinDistBallPaddle))
			{
				
				if(nextVelocity < mInitVelocity)
				{
					float velocityOfBall = (float) Math.sqrt(mBallSpeedX * mBallSpeedX + mBallSpeedY * mBallSpeedY);
				
					mBallSpeedX = mBallX - mPaddleX;
					mBallSpeedY = mBallY - mCanvasHeight;
					
					float newVelocity = (float) Math.sqrt(mBallSpeedX * mBallSpeedX + mBallSpeedY * mBallSpeedY);
					
					mBallSpeedX = (float) ((mBallSpeedX * velocityOfBall / newVelocity) * 1.5);
					mBallSpeedY = (float) ((mBallSpeedY * velocityOfBall / newVelocity) * 1.5);
					
					nextVelocity = nextVelocity + newVelocity;
				}
				else
				{
					float velocityOfBall = (float) Math.sqrt(mBallSpeedX * mBallSpeedX + mBallSpeedY * mBallSpeedY);
					
					mBallSpeedX = mBallX - mPaddleX;
					mBallSpeedY = mBallY - mCanvasHeight;
					
					float newVelocity = (float) Math.sqrt(mBallSpeedX * mBallSpeedX + mBallSpeedY * mBallSpeedY);
					
					mBallSpeedX = (float) (mBallSpeedX * velocityOfBall / newVelocity);
					mBallSpeedY = (float) (mBallSpeedY * velocityOfBall / newVelocity);
					
					nextVelocity = nextVelocity + newVelocity;
				}
			}
				
		}
			
		
	}
}

// This file is part of the course "Begin Programming: Build your first mobile game" from futurelearn.com
// Copyright: University of Reading and Karsten Lundqvist
// It is free software: you can redistribute it and/or modify
// it under the terms of the GNU General Public License as published by
// the Free Software Foundation, either version 3 of the License, or
// (at your option) any later version.
//
// It is is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
// GNU General Public License for more details.
//
// 
// You should have received a copy of the GNU General Public License
// along with it.  If not, see <http://www.gnu.org/licenses/>.
