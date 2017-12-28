package com.cainiao.enumer;

public enum SeckillStatEnum {
    SUCCESS(1, "秒杀成功"),
    END(0, "秒杀结束"),
    REPEAT_KILL(-1, "repeat seckill"),
    INNER_ERROR(-2, "inner error"),
    DATE_REWRITE(-3, "date rewrite");
    private int state;
    private String info;
    SeckillStatEnum(int i, String info) {
        state = i;
        info = info;
    }

    public int getState() {
        return state;
    }


    public String getInfo() {
        return info;
    }


    public static SeckillStatEnum stateOf(int index)
    {
        for (SeckillStatEnum state : values())
        {
            if (state.getState()==index)
            {
                return state;
            }
        }
        return null;
    }
}
