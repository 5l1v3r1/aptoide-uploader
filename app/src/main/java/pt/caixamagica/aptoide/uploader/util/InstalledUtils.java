package pt.caixamagica.aptoide.uploader.util;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import pt.caixamagica.aptoide.uploader.SelectablePackageInfo;

/**
 * Created by neuro on 03-10-2017.
 */

public class InstalledUtils {

  public List<SelectablePackageInfo> nonSystemPackages(boolean ordered, Context context) {
    List<PackageInfo> packs = context.getPackageManager()
        .getInstalledPackages(0);

    Iterator<PackageInfo> infoIterator = packs.iterator();

    LinkedList<PackageInfo> packageInfos = new LinkedList<>();
    while (infoIterator.hasNext()) {
      PackageInfo next = infoIterator.next();
      if (isSystemUpdatedPackage(next) || !isSystemPackage(next)) packageInfos.add(next);
    }

    List<SelectablePackageInfo> selectablePackageInfos = new ArrayList<>();

    selectablePackageInfos.clear();
    for (PackageInfo p : packageInfos) {
      selectablePackageInfos.add(new SelectablePackageInfo(p, context.getPackageManager(), false));
    }

    if (ordered) Collections.sort(selectablePackageInfos, newLastInstallComparator(context));

    return selectablePackageInfos;
  }

  private boolean isSystemUpdatedPackage(PackageInfo packageInfo) {
    int maskUpdade = ApplicationInfo.FLAG_UPDATED_SYSTEM_APP;
    return (packageInfo.applicationInfo.flags & maskUpdade) != 0;
  }

  private boolean isSystemPackage(PackageInfo packageInfo) {
    return ((packageInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) != 0);
  }

  public Comparator<SelectablePackageInfo> newLastInstallComparator(final Context context) {
    return new Comparator<SelectablePackageInfo>() {
      @Override public int compare(SelectablePackageInfo lhs, SelectablePackageInfo rhs) {
        return (int) (getLastInstallDate(rhs, context) / 1000
            - getLastInstallDate(lhs, context) / 1000);
      }
    };
  }

  private long getLastInstallDate(PackageInfo packageInfo, Context context) {
    PackageManager pm = context.getPackageManager();
    String appFile = packageInfo.applicationInfo.sourceDir;
    return new File(appFile).lastModified(); //Epoch Time
  }
}
