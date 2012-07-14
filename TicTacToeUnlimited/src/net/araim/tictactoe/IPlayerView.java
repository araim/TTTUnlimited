package net.araim.tictactoe;

public interface IPlayerView {

	void addOperationListemer(IBoardOperationDispatcher lsnr);

	boolean removeOperationListemer(IBoardOperationDispatcher lsnr);

}
