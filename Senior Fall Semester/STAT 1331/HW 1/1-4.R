library(fBasics)
da=read.table("data/d-3stocks9908.txt", header=T)

returns = da[,2]
log_returns = log(1+returns)
t1 = skewness(log_returns)/ sqrt(6/length(log_returns))
cat("Test statistic =", t1, "\n")

t2 = kurtosis(log_returns)/ sqrt(24/length(log_returns))
cat("Test statistic =", t2, "\n")
