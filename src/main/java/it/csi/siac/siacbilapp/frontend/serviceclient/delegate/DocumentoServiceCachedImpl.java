/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.serviceclient.delegate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.csi.siac.siacbilapp.frontend.serviceclient.delegate.cache.CachedService;
import it.csi.siac.siacbilapp.frontend.serviceclient.delegate.cache.CachedServiceExecutor;
import it.csi.siac.siacbilapp.frontend.serviceclient.delegate.executor.RicercaAttivitaOnereExecutor;
import it.csi.siac.siacbilapp.frontend.serviceclient.delegate.executor.RicercaCausale770Executor;
import it.csi.siac.siacbilapp.frontend.serviceclient.delegate.executor.RicercaCodiceBolloExecutor;
import it.csi.siac.siacbilapp.frontend.serviceclient.delegate.executor.RicercaCodiceCommissioneDocumentoExecutor;
import it.csi.siac.siacbilapp.frontend.serviceclient.delegate.executor.RicercaCodicePCCExecutor;
import it.csi.siac.siacbilapp.frontend.serviceclient.delegate.executor.RicercaCodiceUfficioDestinatarioPCCExecutor;
import it.csi.siac.siacbilapp.frontend.serviceclient.delegate.executor.RicercaNaturaOnereExecutor;
import it.csi.siac.siacbilapp.frontend.serviceclient.delegate.executor.RicercaNoteTesoriereExecutor;
import it.csi.siac.siacbilapp.frontend.serviceclient.delegate.executor.RicercaSommaNonSoggettaExecutor;
import it.csi.siac.siacbilapp.frontend.serviceclient.delegate.executor.RicercaTipoAvvisoExecutor;
import it.csi.siac.siacbilapp.frontend.serviceclient.delegate.executor.RicercaTipoDocumentoExecutor;
import it.csi.siac.siacbilapp.frontend.serviceclient.delegate.executor.RicercaTipoImpresaExecutor;
import it.csi.siac.siacbilapp.frontend.serviceclient.delegate.executor.RicercaTipoOnereExecutor;
import it.csi.siac.siacbilapp.frontend.serviceclient.delegate.keyadapter.RicercaAttivitaOnereKeyAdapter;
import it.csi.siac.siacbilapp.frontend.serviceclient.delegate.keyadapter.RicercaCausale770KeyAdapter;
import it.csi.siac.siacbilapp.frontend.serviceclient.delegate.keyadapter.RicercaCodiceBolloKeyAdapter;
import it.csi.siac.siacbilapp.frontend.serviceclient.delegate.keyadapter.RicercaCodiceCommissioneDocumentoKeyAdapter;
import it.csi.siac.siacbilapp.frontend.serviceclient.delegate.keyadapter.RicercaCodicePCCKeyAdapter;
import it.csi.siac.siacbilapp.frontend.serviceclient.delegate.keyadapter.RicercaCodiceUfficioDestinatarioPCCKeyAdapter;
import it.csi.siac.siacbilapp.frontend.serviceclient.delegate.keyadapter.RicercaNaturaOnereKeyAdapter;
import it.csi.siac.siacbilapp.frontend.serviceclient.delegate.keyadapter.RicercaNoteTesoriereKeyAdapter;
import it.csi.siac.siacbilapp.frontend.serviceclient.delegate.keyadapter.RicercaSommaNonSoggettaKeyAdapter;
import it.csi.siac.siacbilapp.frontend.serviceclient.delegate.keyadapter.RicercaTipoAvvisoKeyAdapter;
import it.csi.siac.siacbilapp.frontend.serviceclient.delegate.keyadapter.RicercaTipoDocumentoKeyAdapter;
import it.csi.siac.siacbilapp.frontend.serviceclient.delegate.keyadapter.RicercaTipoImpresaKeyAdapter;
import it.csi.siac.siacbilapp.frontend.serviceclient.delegate.keyadapter.RicercaTipoOnereKeyAdapter;
import it.csi.siac.siacfin2ser.frontend.webservice.DocumentoService;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AggiornaRelazioneDocumenti;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AggiornaRelazioneDocumentiResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaAttivitaOnere;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaAttivitaOnereResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaCausale770;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaCausale770Response;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaCodiceBollo;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaCodiceBolloResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaCodiceCommissioneDocumento;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaCodiceCommissioneDocumentoResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaCodicePCC;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaCodicePCCResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaCodiceUfficioDestinatarioPCC;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaCodiceUfficioDestinatarioPCCResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaNaturaOnere;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaNaturaOnereResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaNoteTesoriere;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaNoteTesoriereResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaQuoteDaAssociare;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaQuoteDaAssociarePredocumento;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaQuoteDaAssociarePredocumentoResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaQuoteDaAssociareResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaSommaNonSoggetta;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaSommaNonSoggettaResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaTipoAvviso;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaTipoAvvisoResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaTipoDocumento;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaTipoDocumentoResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaTipoImpresa;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaTipoImpresaResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaTipoOnere;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaTipoOnereResponse;

/**
 * Cached version of {@link DocumentoService}.
 * 
 * @author Domenico
 * @version 1.0.0 - 30/09/2014
 *
 */
@Component
@CachedService
public class DocumentoServiceCachedImpl implements DocumentoService {

	@Autowired
	private transient DocumentoService documentoService;

	@Autowired
	private CachedServiceExecutor cachedServiceExecutor;

	@Override
	public RicercaTipoDocumentoResponse ricercaTipoDocumento(RicercaTipoDocumento req) {
		return cachedServiceExecutor.executeService(req, new RicercaTipoDocumentoExecutor(documentoService), new RicercaTipoDocumentoKeyAdapter());
	}

	@Override
	public RicercaCodiceBolloResponse ricercaCodiceBollo(RicercaCodiceBollo req) {
		return cachedServiceExecutor.executeService(req, new RicercaCodiceBolloExecutor(documentoService), new RicercaCodiceBolloKeyAdapter());
	}

	@Override
	public RicercaTipoImpresaResponse ricercaTipoImpresa(RicercaTipoImpresa req) {
		return cachedServiceExecutor.executeService(req, new RicercaTipoImpresaExecutor(documentoService), new RicercaTipoImpresaKeyAdapter());
	}

	@Override
	public RicercaTipoAvvisoResponse ricercaTipoAvviso(RicercaTipoAvviso req) {
		return cachedServiceExecutor.executeService(req, new RicercaTipoAvvisoExecutor(documentoService), new RicercaTipoAvvisoKeyAdapter());
	}

	@Override
	public RicercaNaturaOnereResponse ricercaNaturaOnere(RicercaNaturaOnere req) {
		return cachedServiceExecutor.executeService(req, new RicercaNaturaOnereExecutor(documentoService), new RicercaNaturaOnereKeyAdapter());
	}

	@Override
	public RicercaTipoOnereResponse ricercaTipoOnere(RicercaTipoOnere req) {
		return cachedServiceExecutor.executeService(req, new RicercaTipoOnereExecutor(documentoService), new RicercaTipoOnereKeyAdapter());
	}

	@Override
	public RicercaAttivitaOnereResponse ricercaAttivitaOnere(RicercaAttivitaOnere req) {
		return cachedServiceExecutor.executeService(req, new RicercaAttivitaOnereExecutor(documentoService), new RicercaAttivitaOnereKeyAdapter());
	}

	@Override
	public RicercaCausale770Response ricercaCausale770(RicercaCausale770 req) {
		return cachedServiceExecutor.executeService(req, new RicercaCausale770Executor(documentoService), new RicercaCausale770KeyAdapter());
	}

	@Override
	public RicercaSommaNonSoggettaResponse ricercaSommaNonSoggetta(RicercaSommaNonSoggetta req) {
		return cachedServiceExecutor.executeService(req, new RicercaSommaNonSoggettaExecutor(documentoService), new RicercaSommaNonSoggettaKeyAdapter());
	}
	
	@Override
	public RicercaNoteTesoriereResponse ricercaNoteTesoriere(RicercaNoteTesoriere req) {
		return cachedServiceExecutor.executeService(req, new RicercaNoteTesoriereExecutor(documentoService), new RicercaNoteTesoriereKeyAdapter());
	}

	@Override
	public RicercaCodicePCCResponse ricercaCodicePCC(RicercaCodicePCC req) {
		return cachedServiceExecutor.executeService(req, new RicercaCodicePCCExecutor(documentoService), new RicercaCodicePCCKeyAdapter());
	}

	@Override
	public RicercaCodiceUfficioDestinatarioPCCResponse ricercaCodiceUfficioDestinatarioPCC(RicercaCodiceUfficioDestinatarioPCC req) {
		return cachedServiceExecutor.executeService(req, new RicercaCodiceUfficioDestinatarioPCCExecutor(documentoService), new RicercaCodiceUfficioDestinatarioPCCKeyAdapter());
	}

	@Override
	public RicercaQuoteDaAssociareResponse ricercaQuoteDaAssociare(RicercaQuoteDaAssociare parameters) {
		return documentoService.ricercaQuoteDaAssociare(parameters);
	}

	@Override
	public AggiornaRelazioneDocumentiResponse collegaDocumenti(AggiornaRelazioneDocumenti parameters) {
		return documentoService.collegaDocumenti(parameters);
	}

	@Override
	public AggiornaRelazioneDocumentiResponse scollegaDocumenti(AggiornaRelazioneDocumenti parameters) {
		return documentoService.scollegaDocumenti(parameters);
	}

	@Override
	public RicercaQuoteDaAssociarePredocumentoResponse ricercaQuoteDaAssociarePredocumento(RicercaQuoteDaAssociarePredocumento parameters) {
		return documentoService.ricercaQuoteDaAssociarePredocumento(parameters);
	}

	@Override
	public RicercaCodiceCommissioneDocumentoResponse ricercaCodiceCommissioneDocumento(RicercaCodiceCommissioneDocumento parameters) {
		return cachedServiceExecutor.executeService(parameters, new RicercaCodiceCommissioneDocumentoExecutor(documentoService), new RicercaCodiceCommissioneDocumentoKeyAdapter());
	}

}
