x <- 1:50 / 10
n <- 20
y <- 36
z <- sum(dpois(y, lambda = n*x)/50)
plot(x, (dpois(y, lambda = n*x)/50/z))
exp <- sum(x*dpois(y, lambda = n*x)/50/z)
v <- sum((exp-x)^2 * dpois(y, lambda = n*x)/50/z)
stddev <- sqrt(v)
confidence_lower <- exp - 1.96*stddev
confidence_higher <- exp + 1.96*stddev

