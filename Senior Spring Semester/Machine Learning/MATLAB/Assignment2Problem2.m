x = 0:20;
y1 = poisspdf(x,2);
y2 = poisspdf(x,6);

poisson = readmatrix('poisson.txt');
poisson = poisson(:,3);
lambda = mean(poisson)

bar(x,y2,1);
x1 = 0:0.5:50;
x2 = 2:0.02:8;

y3 = gampdf(x1,3,5);
plot(x1,y3);
a=3;
b=5;
x1=0:0.1:10;
y4 = gampdf(x1, a+sum(poisson), b/(length(poisson)*b+1));
y5 = normpdf(x2, mean(poisson), std(poisson)/sqrt(length(poisson)));
plot(x1,y4);
title("Posterior Distribution(a=3,b=5)")