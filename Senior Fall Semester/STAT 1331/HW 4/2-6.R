
power6 = read.table("power6.txt", header = F)
pow = power6[, 1]

Box.test(pow, lag=24, type = 'Ljung')
acf(pow)
pacf(pow)
acf(diff(pow))
pacf(diff(pow))
acf(diff(pow, 12))
pacf(diff(pow, 12))

m1 = arima(pow, order = c(1, 0, 1), seasonal = list(order = c(0, 0, 1), period = 12))
m1
tsdiag(m1, gof = 36)
predict(m1, 24)
