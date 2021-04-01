library('magrittr')
library('coda')
install.packages(('MCMCpack'))
library(MCMCpack)


schools = lapply(1:8, function(i) {
  table = read.table(paste0('school', as.character(i), '.dat'))
  table[,1] %>% as.numeric
})

schools.raw = do.call(rbind, schools)
print(schools.raw)
n = sv = ybar = rep(0, 8)
m = 8


mu0 = 0.163
g20 = 5
t20 = 10
nu0 = 2
s20 = 15
eta0 = 2

for(i in 1:8){
  ybar[i] = mean(schools[[i]])
  n[i] = length(schools[[i]])
  sv = var(schools[[i]])
}

theta<-ybar
sigma2<-mean(sv)
mu<-mean(theta)
tau2<-var(theta)


set.seed(1)
S<-5000
THETA<-matrix( nrow=S,ncol=m)
MST<-matrix( nrow=S,ncol=3)

## Part a
for(s in 1:S) 
{

  for(j in 1:m) 
  {
    vtheta<-1/(n[j]/sigma2+1/tau2)
    etheta<-vtheta*(ybar[j]*n[j]/sigma2+mu/tau2)
    theta[j]<-rnorm(1,etheta,sqrt(vtheta))
  }
  
  nun<-nu0+sum(n)
  ss<-nu0*s20;
  for(j in 1:m){
    ss<-ss+sum((Y[[j]]-theta[j])^2)
  }
  sigma2<-1/rgamma(1,nun/2,ss/2)
  
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
  MST[s,]<-c(mu,sigma2,tau2)
  
}
mcmc1<-list(THETA=THETA,MST=MST)
stationarity.plot(MST[,1],xlab="iteration",ylab=expression(mu))
stationarity.plot(MST[,2],xlab="iteration",ylab=expression(sigma^2))
stationarity.plot(MST[,3],xlab="iteration",ylab=expression(tau^2))
effectiveSize(MST)

## Part b
t(apply(MST, MARGIN = 2, FUN = quantile, probs = c(0.025, 0.5, 0.975)))

plot(density(MST[,1],adj=2),xlab=expression(mu), main="",lwd=2,
     ylab=expression(paste(italic("p("),mu,"|",italic(y[1]),"...",italic(y[m]),")")))
abline( v=quantile(MST[,1],c(.025,.5,.975)),col="gray",lty=c(3,2,3) )
ds<-seq(0,12, by=0.1)
lines(ds,dnorm(ds,mu0,sqrt(g20)),lwd=2,col="gray" )
legend(10,.4,legend=c("prior","posterior"),lwd=c(2,2),col=c("black","gray"),
       bty="n")

plot(density(MST[,2],adj=2),xlab=expression(sigma^2),main="", lwd=2,
     ylab=expression(paste(italic("p("),sigma^2,"|",italic(y[1]),"...",italic(y[m]),")")))
abline( v=quantile(MST[,2],c(.025,.5,.975)),col="gray",lty=c(3,2,3) )
ds<-seq(0,25, by=0.1)
lines(ds,dinvgamma(ds, nu0 / 2, nu0 * s20 / 2),lwd=2,col="gray" )
legend(20,.2,legend=c("prior","posterior"),lwd=c(2,2),col=c("black","gray"),
       bty="n")


plot(density(MST[,3],adj=2),xlab=expression(tau^2),main="",lwd=2,
     ylab=expression(paste(italic("p("),tau^2,"|",italic(y[1]),"...",italic(y[m]),")")))
abline( v=quantile(MST[,3],c(.025,.5,.975)),col="gray",lty=c(3,2,3) )
ds<-seq(0,50, by=0.1)
lines(ds,dinvgamma(ds, eta0 / 2, eta0 * t20 / 2),lwd=2,col="gray" )
legend(30,.15,legend=c("prior","posterior"),lwd=c(2,2),col=c("black","gray"),
       bty="n")

## Part c
t20_prior = (1/rgamma(10000, eta0 / 2, eta0 * t20 / 2))
s20_prior = (1/rgamma(10000, nu0 / 2, nu0 * s20 / 2))
prior = t20_prior / (t20_prior + s20_prior)

plot(density(MST[,3]/(MST[,3] + MST[,2]),adj=2),xlab=expression(tau^2/(tau^2 + sigma^2)),main="",lwd=2,
     ylab= 'density', xlim = c(0.0, 1.00))
lines(density(prior),col="gray" )
legend(.8,3,legend=c("prior","posterior"),lwd=c(2,2),col=c("black","gray"),
       bty="n")


## Part d

mean(THETA[,7] < THETA[,6])
mean((THETA[, 7] < THETA[, -7]) %>% apply(MARGIN = 1, FUN = all))

## Part e

post_mean = colMeans(THETA)
plot(ybar, post_mean)

abline(lm(post_mean ~ ybar))
total_sum = 0
for(i in 1:8){
  total_sum = total_sum + sum(Y[[i]])
}
total_mean = total_sum/sum(n)
abline(a=total_mean, b=0)
abline(a=mean(MST[,1]), b=0, col='red')
mean(MST[,1])

stationarity.plot<-function(x,...){
  
  S<-length(x)
  scan<-1:S
  ng<-min( round(S/100),10)
  group<-S*ceiling( ng*scan/S) /ng
  
  boxplot(x~group,...)               }
