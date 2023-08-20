package city.thefloating.floatyplugin;

import cloud.commandframework.execution.CommandExecutionCoordinator;
import cloud.commandframework.paper.PaperCommandManager;
import com.google.inject.Inject;
import dev.tehbrian.tehlib.paper.cloud.PaperCloudService;
import org.bukkit.command.CommandSender;

import java.util.function.Function;

public class CommandService extends PaperCloudService<CommandSender> {

  private final FloatyPlugin plugin;

  @Inject
  public CommandService(
      final FloatyPlugin plugin
  ) {
    this.plugin = plugin;
  }

  /**
   * Instantiates {@link #commandManager}.
   *
   * @throws IllegalStateException if {@link #commandManager} is already instantiated
   * @throws Exception             if something goes wrong during instantiation
   */
  public void init() throws Exception {
    if (this.commandManager != null) {
      throw new IllegalStateException("The CommandManager is already instantiated.");
    }

    this.commandManager = new PaperCommandManager<>(
        this.plugin,
        CommandExecutionCoordinator.simpleCoordinator(),
        Function.identity(),
        Function.identity()
    );
  }

}
