library(fBasics)
da=read.table("data/m-gm3dx7508.txt", header=T)

returns = da[,2:5]

apply(returns*100, 2, basicStats)

log_returns = log(1+returns)

apply(log_returns*100, 2, basicStats)

apply(log_returns, 2, t.test)
