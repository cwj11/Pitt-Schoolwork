ibmsp = read.table("d-ibm3dxwkdays8008.txt", header =T)
sp = ibmsp$sp * 100

M = ibmsp$M
Tu = ibmsp$T
W = ibmsp$W
Th = ibmsp$R
Fr = ibmsp$F

m1 = lm(sp ~ M + Tu + W + Th + Fr + 0)
summary(m1)
Box.test(m1$residuals, lag = 12, type = 'Ljung')
acf(m1$residuals)
pacf(m1$residuals)
