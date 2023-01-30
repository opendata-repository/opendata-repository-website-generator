package io.github.fabricetheytaz.opendata.repository;

import java.io.IOException;
import java.sql.SQLException;
import org.apache.commons.lang3.function.FailableBiConsumer;
import io.github.fabricetheytaz.schema.org.AutoRepair;
import io.github.fabricetheytaz.schema.org.HairSalon;
import io.github.fabricetheytaz.schema.org.LocalBusiness;
import io.github.fabricetheytaz.schema.org.Thing;
import io.github.fabricetheytaz.schema.org.database.SchemaOrgDatabase;
import io.github.fabricetheytaz.websitegenerator.IConfiguration;
import io.github.fabricetheytaz.websitegenerator.WebSiteGenerator;
import io.github.fabricetheytaz.websitegenerator.engine.IRenderer;
import io.github.fabricetheytaz.websitegenerator.engine.ITemplateEngine;
import io.github.fabricetheytaz.websitegenerator.freemarker.FreeMarkerTemplateEngine;

public final class OpenDataRepositoryWebSiteGgnerator extends WebSiteGenerator
	{
	//private static final IStringRenderer<Thing> JSON_RENDERER = new GsonEngine<Thing>().getRenderer();

	private final ITemplateEngine<Object, String> templateEngine;

	private OpenDataRepositoryWebSiteGgnerator(IConfiguration configuration) throws IOException
		{
		super(configuration);

		templateEngine = new FreeMarkerTemplateEngine(configuration.getTemplatesDirectory());
		}

	public void build() throws SQLException, IOException
		{
		html();

		//final Path source = stringToRealPath.apply(configuration.getSource());
		//final Path destination = stringToRealPath.apply(configuration.getDestination());

		/*
		try (final SchemaOrgDatabase database = new SchemaOrgDatabase())
			{
			//getJsonConverter(destination)

			database.getAll(LocalBusiness.class, this::xyz);
			database.getAll(Organization.class, this::xyz);
			database.getAll(BusStop.class, this::xyz);

			//Files.writeString(destination.resolve("things.json"), database.getAllAsJSON(), StandardCharsets.UTF_8);
			}
		*/
		}

	private <T extends Thing> FailableBiConsumer<Integer, T, IOException> html(final String template) throws IOException
		{
		final IRenderer<Object, String> renderer = templateEngine.getRenderer(template);

		return (id, thing) ->
			{
			final String html = renderer.render(thing);

			System.out.println(html);
			};
		}

	private void html() throws SQLException, IOException
		{
		try (final SchemaOrgDatabase database = new SchemaOrgDatabase())
			{
			database.getAll(LocalBusiness.class, html("local-business.twig"));
			database.getAll(AutoRepair.class, html("local-business.twig"));
			database.getAll(HairSalon.class, html("local-business.twig"));
			}
		}

	@SuppressWarnings("unused")
	private <T extends Thing> void xyz(final Integer id, final T thing) throws IOException
		{
		//final String filename = String.format("%d.json", id);
		//final Path path = destination.resolve(filename);
		//Files.writeString(path, JSON_RENDERER.render(thing), StandardCharsets.UTF_8);
		}

	private static class Conf implements IConfiguration
		{
		@Override
		public String getDestination()
			{
			return null;
			}

		@Override
		public String getSource()
			{
			return null;
			}

		@Override
		public String getTemplatesDirectory()
			{
			return "templates";
			}
		}

	public static void main(final String[] args)
		{
		try
			{
			new OpenDataRepositoryWebSiteGgnerator(new Conf()).build();
			}
		catch (final Exception ex)
			{
			ex.printStackTrace();
			}
		}
	}
