/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.ui.model.previmpacc;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

import it.csi.siac.siacbilapp.frontend.ui.util.format.FormatUtils;
import it.csi.siac.siacbilser.model.ImportiCapitolo;
import it.csi.siac.siacbilser.model.ImportiCapitoloEnum;
import it.csi.siac.siacbilser.model.ImportiCapitoloUG;

/**
 * The Class RisultatiRicercaPrevisioneImpegnatoCapUGModel.
 *
 * @author elisa
 * @version 1.0.0 15 ott 2021
 */
public class RisultatiRicercaPrevisioneImpegnatoCapUGModel extends RisultatiRicercaPrevisioneImpegnatoAccertatoModel	 {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = -7856292064445522647L;
	/*Totale Importi */
	private ImportiCapitoloUG totaleImporti = new ImportiCapitoloUG();
	
	/** Costruttore vuoto di default */
	public RisultatiRicercaPrevisioneImpegnatoCapUGModel() {
		super();
		setTitolo("Risultati di ricerca previsione impegnato");
	}

	public ImportiCapitoloUG getTotaleImporti() {
		return totaleImporti;
	}

	public void setTotaleImporti(ImportiCapitoloUG totaleImporti) {
		this.totaleImporti = totaleImporti;
	}

	@Override
	public String getDescrizioneImportoDerivato() {
		return "Impegnato";
	}

	@Override
	public String getImportoDerivatoAnno0() {
		ImportiCapitolo importiCapitolo0 = getImportiCapitolo0();
		if(importiCapitolo0 instanceof ImportiCapitoloUG) {
			BigDecimal derivatoAnno0 = ((ImportiCapitoloUG) importiCapitolo0).getImpegnatoEffettivoUGAnno1();
			return FormatUtils.formatCurrency(derivatoAnno0);
		}
		return "N.D.";
	}

	@Override
	public String getImportoDerivatoAnno1() {
		ImportiCapitolo importiCapitolo0 = getImportiCapitolo0();
		if(importiCapitolo0 instanceof ImportiCapitoloUG) {
			BigDecimal derivatoAnno1 = ((ImportiCapitoloUG) importiCapitolo0).getImpegnatoEffettivoUGAnno2();
			return FormatUtils.formatCurrency(derivatoAnno1);
		}
		return "N.D.";
	}

	@Override
	public String getImportoDerivatoAnno2() {
		ImportiCapitolo importiCapitolo0 = getImportiCapitolo0();
		if(importiCapitolo0 instanceof ImportiCapitoloUG) {
			BigDecimal derivatoAnno0 = ((ImportiCapitoloUG) importiCapitolo0).getImpegnatoEffettivoUGAnno3();;
			return FormatUtils.formatCurrency(derivatoAnno0);
		}
		return "N.D.";
	}
	
	@Override
	protected Set<ImportiCapitoloEnum> caricaImportiDerivatiRichiesti() {
		Set<ImportiCapitoloEnum> importiDerivati = new HashSet<ImportiCapitoloEnum>();
		importiDerivati.add(ImportiCapitoloEnum.impegnatoEffettivoUGAnno1);
		importiDerivati.add(ImportiCapitoloEnum.impegnatoEffettivoUGAnno2);
		importiDerivati.add(ImportiCapitoloEnum.impegnatoEffettivoUGAnno3);
		return importiDerivati;
	}
	
	
}

	
	
	
	