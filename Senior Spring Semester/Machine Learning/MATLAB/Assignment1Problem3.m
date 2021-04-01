pima = readmatrix('pima.txt');
norm = normalize(pima(:,3))
mean(pima(:,3))
std(pima(:,3))
discretize_attribute(pima(:,3), 10)2






function n = normalize(v)
    m = mean(v);
    s = std(v);
    n = (v-m)/s
end

function d = discretize_attribute(v, bins)
    v_sort = sort(v);
    bin_vals = zeros(1,bins);
    for i = 1:bins
        bin_vals(i) = v_sort(round(length(v)/bins*i));
    end
    d = zeros(1,length(v));
    for i = 1:length(v)
        num = 1;
        while num <= bins & v(i) >= bin_vals(num)
           num = num+1; 
        end
        d(i) = num;
    end
    d;
end