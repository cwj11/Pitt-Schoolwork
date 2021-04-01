data = readmatrix('NDE_data.txt');
test = readmatrix('NDE_test.txt');
data = data(:,4);
test = test(:,4);
hist(data,20);
parzen = zeros(length(test), 1);
x = 0:0.01:1;
y = zeros(length(x),1);
h = 0.1;
for i = 1:length(test)
   parzen(i) = Parzen_window(test(i), h, data);
end
for i = 1:length(x)
   y(i) = Parzen_window(x(i), h, data);
end
parzen
kn = zeros(length(test),1);
for i = 1:length(test)
    kn(i) = kNN(test(i), 3, data);
end
y1 = zeros(length(x),1);
for i = 1:length(x)
    y1(i) = kNN(x(i), 3, data);
end
kn
plot(x,y1);
title("kNN Distribution(k=3)");















function p = Parzen_window(x,h,D)
    k = 0;
    for i = 1:length(D)
       if abs(x - D(i))/h <= 0.5
          k = k+1; 
       end
    end
    p = k/(h*length(D));
end

function p = kNN(x,k,D)
    
    counter = 0;
    p = 0;
    D = sort(D);
    a = 1;
    b = 1;
    while a <= length(D) && D(a) < x
       a = a+1;
       b = b+1;
    end
    a = a-1;
    while counter < k
        counter = counter+1;
        if a < 1 || a > length(D)
           p = D(b) - x;
           b = b+1;
           continue
        end
        if b < 1 || b > length(D)
           p = x - D(a);
           a = a-1;
           continue
        end
        if x - D(a) < D(b) - x
            p = x - D(a);
            a=a-1;
        else
            p = D(b) - x;
            b=b+1;
        end
        
        
    end
    p = p*2;
    p = k/(length(D)*p);
end










