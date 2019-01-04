package io.github.ranolp.boardka.api;

/*********************************************************
 *                                                       *
 *                      ! WARNING !                      *
 *              This class used internally,              *
 *            You MUST NOT use this directly.            *
 *                                                       *
 *********************************************************/
final class _EmptyBoardka extends Boardka {
    private _EmptyBoardka() {
    }

    @Override
    public Sidebar sidebar() {
        return _EmptySidebar.SingletonHolder.INSTANCE;
    }

    @Override
    public boolean isDisposed() {
        return true;
    }

    @Override
    public void dispose(boolean safe) {
        // do nothing
    }

    static final class SingletonHolder {
        static final _EmptyBoardka INSTANCE = new _EmptyBoardka();
    }

    @Override
    public String toString() {
        return "EmptyBoardka";
    }
}
