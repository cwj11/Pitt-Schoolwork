library(fBasics)
da=read.table("data/m-gm3dx7508.txt", header=T)

returns = da[,5]
log_returns = log(1+returns)
annual_return = mean(log_returns)*12
cat("Annual Log Return =", annual_return, "\n")

value = exp(annual_return*34)
cat("Investment Value = $", value, "\n")
