install.packages(fitdistrplus)
library('magrittr')
library(fitdistrplus)
library(MASS)

setwd('D:\\Schoolwork\\Senior First Semester\\STAT 1651')
bd = read.table('Bat.dat')

bd1 = bd[bd$AB.1 >= 10 & bd$AB.2 >= 10, ]
bd_pitch = bd1[bd1$Pitcher == 1, ]
bd1 = bd1[bd1$Pitcher == 0, ]
bd1 = problem2(bd1)




problem2 <- function(x) {
  x$p.1 = x$H.1/x$AB.1
  x$p.2 = x$H.2/x$AB.2
  
  xbar = mean(x$p.1)
  stddev = sd(x$p.1)
  alpha0 <- xbar*((xbar*(1 - xbar)/stddev^2) - 1)
  beta0 <- (1 - xbar)*((xbar*(1 - xbar)/stddev^2) - 1)
  
  x$post_pred = (alpha0+x$H.1)/(alpha0+beta0+x$AB.1)
  
  x
}

mse = sum((bd1$p.2 - bd1$post_pred)*(bd1$p.2 - bd1$post_pred))/length(bd1$p.2)
print(bd1$post_pred)

bd1$X.1 = asin(sqrt((bd1$H.1+1/4)/(bd1$AB.1+1/2)))




mu0 = 0.24
g20 = 5
t20 = 10
nu0 = 2
s20 = 15
eta0 = 2



theta<-bd1$X.1
sigma2<-1/(4*bd1$AB.1)
mu<-mean(bd1$X.1)
tau2<-var(theta)

m = length(bd1$X.1)
set.seed(1)
S<-5000
THETA<-matrix( nrow=S,ncol=m)
MST<-matrix( nrow=S,ncol=3)

for(s in 1:S) 
{
  
  for(j in 1:m) 
  {
    vtheta<-1/(1/sigma2[j]+1/tau2)
    etheta<-vtheta*(bd1$X.1[j]/sigma2[j]+mu/tau2)
    theta[j]<-rnorm(1,etheta,sqrt(vtheta))
  }
  
  nun<-nu0+sum(n)
  ss<-nu0*s20;
  for(j in 1:494){
    ss<-ss+sum((bd1$X.1[j]-theta[j])^2)
  }
  
  #sample a new value of mu
  vmu<- 1/(m/tau2+1/g20)
  emu<- vmu*(m*mean(theta)/tau2 + mu0/g20)
  mu<-rnorm(1,emu,sqrt(vmu)) 
  
  # sample a new value of tau2
  etam<-eta0+m
  ss<- eta0*t20 + sum( (theta-mu)^2 )
  tau2<-1/rgamma(1,etam/2,ss/2)
  
  #store results
  THETA[s,]<-theta
  MST[s,]<-c(mu,sigma2[1],tau2)
  
}

for(i in 1:m){
  bd1$hier_pred[i] = sin(mean(THETA[,i]))^2
}
print(bd1$hier_pred)

mse_beta = sum((bd1$p.2 - bd1$post_pred)^2)/length(bd1$p.2)
mse2_hier = sum((bd1$p.2 - bd1$hier_pred)^2)/length(bd1$p.2)

