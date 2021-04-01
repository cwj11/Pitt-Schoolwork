pima = readmatrix('pima.txt');
vals = zeros(1,20);
for i = 1:20
    vals(i) = length(divideset1(pima,0.66));
end
vals
mean(vals)
length(divideset2(pima,0.66))


function [train,test] = divideset1(m,p)
    ind1 = [];
    ind2 = [];
    for i = 1:length(m(:,1))
        if rand<p
            ind1(end+1) = i;
        else
            ind2(end+1) = i;
        end
    end
    train = m(ind1,:);
    test = m(ind2,:);
end

function [train,test] = divideset2(m,p)
    ind = randperm(length(m(:,1)));
    train = m(ind(1:round(length(m)*p)),:);
    test = m(ind(round(length(m)*p:end)),:);
end