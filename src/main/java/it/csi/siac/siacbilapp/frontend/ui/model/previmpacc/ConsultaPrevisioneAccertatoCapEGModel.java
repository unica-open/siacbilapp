/**
 * SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
 * SPDX-License-Identifier: EUPL-1.2
 */
package it.csi.siac.siacbilapp.frontend.ui.model.previmpacc;

import java.math.BigDecimal;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.Set;

import it.csi.siac.siacbilapp.frontend.ui.util.format.FormatUtils;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioModulareCapitoloEntrataGestione;
import it.csi.siac.siacbilser.model.CapitoloEntrataGestione;
import it.csi.siac.siacbilser.model.ImportiCapitolo;
import it.csi.siac.siacbilser.model.ImportiCapitoloEG;
import it.csi.siac.siacbilser.model.ImportiCapitoloEnum;
import it.csi.siac.siaccorser.model.TipologiaClassificatore;
import it.csi.siac.siacfin2ser.model.CapitoloEntrataGestioneModelDetail;

/**
 * The Class RicercaPrevisioneImpegnatoAccertatoModel.
 *
 * @author elisa
 * @version 1.0.0 15 ott 2021
 */
public class ConsultaPrevisioneAccertatoCapEGModel extends ConsultaPrevisioneImpegnatoAccertatoModel {
	
	/** per la serializzazione */
	private static final long serialVersionUID = -8384743304142058860L;
	
	public ConsultaPrevisioneAccertatoCapEGModel() {
		super();
		setTitolo("Consulta previsione accertato");
	}
	
	@Override
	public String getDescrizioneImportoDerivato() {
		// TODO Auto-generated method stub
		return "Accertato";
	}

	@Override
	public String getImportoDerivatoAnno0() {
		ImportiCapitolo importiCapitolo0 = getImportiCapitolo0();
		if(importiCapitolo0 instanceof ImportiCapitoloEG) {
			BigDecimal stanziamento = ((ImportiCapitoloEG) importiCapitolo0).getStanziamento();
			BigDecimal disponibilitaAccertare = ((ImportiCapitoloEG) importiCapitolo0).getDisponibilitaAccertareAnno1();
			BigDecimal accertato = stanziamento != null && disponibilitaAccertare != null? stanziamento.subtract(disponibilitaAccertare) 
					: stanziamento != null? stanziamento 
							: disponibilitaAccertare != null? disponibilitaAccertare.negate() : BigDecimal.ZERO ;
			return FormatUtils.formatCurrency(accertato);
		}
		return "N.D.";
	}

	@Override
	public String getImportoDerivatoAnno1() {
		ImportiCapitolo importiCapitolo1 = getImportiCapitolo1();
		ImportiCapitolo importiCapitolo0 = getImportiCapitolo0();
		if(importiCapitolo0 instanceof ImportiCapitoloEG && importiCapitolo1 instanceof ImportiCapitoloEG ) {
			BigDecimal stanziamento = ((ImportiCapitoloEG) importiCapitolo1).getStanziamento();
			BigDecimal disponibilitaAccertare = ((ImportiCapitoloEG) importiCapitolo0).getDisponibilitaAccertareAnno2();
			BigDecimal accertato = stanziamento != null && disponibilitaAccertare != null? stanziamento.subtract(disponibilitaAccertare) 
					: stanziamento != null? stanziamento 
							: disponibilitaAccertare != null? disponibilitaAccertare.negate() : BigDecimal.ZERO ;
			return FormatUtils.formatCurrency(accertato);
		}
		return "N.D.";
	}

	@Override
	public String getImportoDerivatoAnno2() {
		ImportiCapitolo importiCapitolo2 = getImportiCapitolo2();
		ImportiCapitolo importiCapitolo0 = getImportiCapitolo0();
		if(importiCapitolo0 instanceof ImportiCapitoloEG && importiCapitolo2 instanceof ImportiCapitoloEG ) {
			//task-116
			BigDecimal stanziamento = ((ImportiCapitoloEG) importiCapitolo2).getStanziamento();
			BigDecimal disponibilitaAccertare = ((ImportiCapitoloEG) importiCapitolo0).getDisponibilitaAccertareAnno3();
			BigDecimal accertato = stanziamento != null && disponibilitaAccertare != null? stanziamento.subtract(disponibilitaAccertare) 
					: stanziamento != null? stanziamento 
							: disponibilitaAccertare != null? disponibilitaAccertare.negate() : BigDecimal.ZERO ;
			return FormatUtils.formatCurrency(accertato);
		}
		return "N.D.";
	}
	
	@Override
	protected Set<ImportiCapitoloEnum> caricaImportiDerivatiRichiesti() {
		Set<ImportiCapitoloEnum> importiDerivati = new HashSet<ImportiCapitoloEnum>();
		importiDerivati.add(ImportiCapitoloEnum.disponibilitaAccertareAnno1);
		importiDerivati.add(ImportiCapitoloEnum.disponibilitaAccertareAnno2);
		
		importiDerivati.add(ImportiCapitoloEnum.disponibilitaAccertareAnno3);
		return importiDerivati;
	}
	
	public RicercaDettaglioModulareCapitoloEntrataGestione creaRequestRicercaDettaglioModulareCapitoloEntrataGestione() {
		RicercaDettaglioModulareCapitoloEntrataGestione req = creaRequest(RicercaDettaglioModulareCapitoloEntrataGestione.class);
		req.setCapitoloEntrataGestione(new CapitoloEntrataGestione());
		req.getCapitoloEntrataGestione().setUid(getUidCapitolo());
		req.setImportiDerivatiRichiesti(EnumSet.noneOf(ImportiCapitoloEnum.class));
		req.setModelDetails(CapitoloEntrataGestioneModelDetail.Stato, CapitoloEntrataGestioneModelDetail.Classificatori, CapitoloEntrataGestioneModelDetail.Categoria, CapitoloEntrataGestioneModelDetail.AnnoCreazione );
		req.setTipologieClassificatoriRichiesti(TipologiaClassificatore.CDC, TipologiaClassificatore.CDC, TipologiaClassificatore.PDC_IV, TipologiaClassificatore.PDC_V);
		return req;
	}

	@Override
	public String getIdentificativoPrevisione() {
		return "PrevisioneAccertatoCapEG";
	}
	

}
